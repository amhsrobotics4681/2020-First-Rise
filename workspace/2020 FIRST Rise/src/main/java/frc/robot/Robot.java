/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.analog.adis16470.frc.ADIS16470_IMU;
import com.analog.adis16470.frc.ADIS16470_IMU.IMUAxis;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;

public class Robot extends TimedRobot {
    private Joystick controllerDriver;
    private Joystick controllerShooter;
    private Climber m_climber;
    private Wheel m_wheel;
    private BallSystem m_ball;
    private Victor m_left;
    private Victor m_right;
    private DifferentialDrive m_drive;
    private Counter counter;
    private boolean autoShoot;
    private boolean aligning;
    private double vTranslational;
    private double vRotational;
    private int autoTimer;
    private CameraServer m_cameraServer;
    private String drivingStatus;
    private String autoStrategy;
    private SendableChooser<String> m_chooser = new SendableChooser<>();
    private ADIS16470_IMU m_gyro;
    private Index m_index;
    private Intake m_intake;
    private Shooter m_shooter;

    @Override
    public void robotInit() {
        controllerDriver = new Joystick(0);
        controllerShooter = new Joystick(1);
        m_climber = new Climber();
        m_climber.climberInit();
        m_wheel = new Wheel();
        m_wheel.wheelInit();
        m_ball = new BallSystem();
        m_ball.ballSystemInit();
        m_index = new Index();
        m_index.indexInit();
        m_intake = new Intake();
        m_intake.intakeInit();
        m_left = new Victor(Constants.PWM_TreadsLeft);
        m_right = new Victor(Constants.PWM_TreadsRight);
        m_right.setInverted(false);
        m_left.setInverted(true);
        m_drive = new DifferentialDrive(m_left, m_right);
        counter = new Counter(Constants.DIO_LIDAR);
        counter.setMaxPeriod(1.0);
        counter.setSemiPeriodMode(true);
        counter.reset();
        vTranslational = 0;
        vRotational = 0;
        drivingStatus = "Driving";
        m_chooser.setDefaultOption("Shoot, Fd", "Urus");
        m_chooser.addOption("Shoot, Turn, Fd", "Diablo");
        m_chooser.addOption("Shoot, Collect", "Aventador");
        m_chooser.addOption("Shoot, Collect, Shoot", "Veneno");
        SmartDashboard.putData("Auto Choices", m_chooser);
        m_gyro = new ADIS16470_IMU();
        m_cameraServer.getInstance().startAutomaticCapture("Shooting Camera", 0);
        m_cameraServer.getInstance().startAutomaticCapture("Collecting Camera", 1);
    }

    @Override
    public void autonomousInit() {
        autoTimer = 0;
        autoShoot = false;
        aligning = true;
        m_gyro.setYawAxis(IMUAxis.kY);
        m_ball.toggleIntake();
        m_ball.reviveIntake();
        autoStrategy = m_chooser.getSelected();
        autoStrategy = "Veneno";
        m_drive.arcadeDrive(0, 0);
        m_drive.setSafetyEnabled(false);
        //Urus: Shoot 3 balls then drive forward off the line (Works in all positions)
        //Diablo: Shoot 3 balls, turn around, then drve forwards off the line (works in all positions)
        //Aventador: Shoot 3 balls, turn around, drive to collect 3 balls in trench (works on right side) (Does not require Limelight)
        //Veneno: Shoots 3 balls, turns around, drive to collect 3 balls in trench and then shoots (right side) (requires limelight)
    }

    @Override
    public void autonomousPeriodic() {
        m_ball.mainMethod();
        autoTimer++;
        if (!autoShoot) { 
            m_ball.resetShooter();
            autoShoot = true;
        }
        System.out.println(autoTimer);
        if (autoStrategy.equals("Urus")){
            if (autoTimer > 200 && autoTimer < 350) {
                m_drive.arcadeDrive(0, -0.7);
            } else {
                m_drive.arcadeDrive(0,0);
                System.out.println("yo wassup");
            }
        } else if (autoStrategy.equals("Diablo")){
            if (autoTimer > 250 && autoTimer < 450){
                if ((m_gyro.getAngle()) < 180) {
                    m_drive.arcadeDrive(-0.7, 0);
                } else {
                    m_drive.arcadeDrive(0, -0.8);
                }
            }
            else { 
                m_drive.arcadeDrive(0,0);
            }
        } else if (autoStrategy.equals("Aventador")){    
            if (autoTimer > 500) {
                if (m_gyro.getAngle() < 187) {
                    m_drive.arcadeDrive(-0.7, 0);
                } else {
                    m_drive.arcadeDrive(0, -0.5);
                }
            } else if (autoTimer > 280) {
                if ((m_gyro.getAngle()) < 180) {
                    m_drive.arcadeDrive(-0.7, 0);
                } else {
                    m_drive.arcadeDrive(0, -0.8);
                }
            } else {
                m_drive.arcadeDrive(0,0);
            }
        } 
        //WE ARE NOT ACTUALLY ADJUSTING SCREW HEIGHT, NEED TO FIX
        else if (autoStrategy.equals("Veneno")){
            if (autoTimer > 670) {
                if (m_gyro.getAngle() > 20) { // will use limelight target exists boolean
                    m_drive.arcadeDrive(1.0, 0);
                } else {
                    Update_Limelight_Tracking(); // need to tune above gyro code : check drift
                }
                if (!aligning)
                    m_ball.resetShooter();
            } else if (autoTimer > 440) {
                if (m_gyro.getAngle() < 190) {
                    m_drive.arcadeDrive(-0.7, 0);
                } else {
                    m_drive.arcadeDrive(0, -0.55);
                }
            } else if (autoTimer > 230) {
                if ((m_gyro.getAngle()) < 180) {
                    m_drive.arcadeDrive(-0.7, 0);
                } else {
                    m_drive.arcadeDrive(0, -1.0);
                }
            } else {
                m_drive.arcadeDrive(0,0);
            }
        } else {
            autoStrategy = "Urus";
        }
    } 
    
    @Override
    public void teleopInit() {
        m_ball.toggleSpit();
        m_ball.toggleIntake();
        
    }

    @Override
    public void teleopPeriodic() {
        //System.out.println("Ball Count: " + m_ball.ballCount()+", Distance: " + (int) getDistance() + ", Color: " + m_wheel.getColor());
        m_ball.mainMethod();
        m_intake.mainMethod();
        m_shooter.mainMethod();
        m_index.mainMethod(m_intake.getIndexSpinning(), m_shooter.getIndexSpinning());
        m_wheel.mainMethod();
        m_climber.mainMethod();

        // CONTROLS
        if (drivingStatus.equals("Driving")) {//Incramental Acceleration to prevent falling over
            m_ball.screwSpeed(-controllerDriver.getRawAxis(3));
            if(vTranslational < controllerDriver.getRawAxis(1))
                vTranslational += Constants.kSpeedCurve;
            if(vTranslational > controllerDriver.getRawAxis(1))
                vTranslational -= Constants.kSpeedCurve;
            if(vRotational < controllerDriver.getRawAxis(2))
                vRotational += Constants.kSpeedCurve;
            if(vRotational > controllerDriver.getRawAxis(2))
                vRotational -= Constants.kSpeedCurve;
        } else if (drivingStatus.equals("Shooting")) {//Starts autodriving, adjusts screw
            Update_Limelight_Tracking();
            m_ball.convertElevation((int) getDistance());
            m_ball.adjustScrew();
            if (!aligning && m_ball.screwAtElevation) {
                // yeah, forget setter/getter functions
                m_ball.resetShooter();
            }
        } else if (drivingStatus.equals("Climbing")) {//Controls shift to other remote and everything is dialed down to half power
            vTranslational = (controllerShooter.getRawAxis(1)/2);
            vRotational = (controllerShooter.getRawAxis(0)/2);
            if (controllerShooter.getRawButton(6)) {
                m_climber.extending();
            } else if (controllerShooter.getRawButton(7)) {
                m_climber.contracting();
            } else {
                m_climber.stop();
            }
        }
        
        m_drive.arcadeDrive(-vRotational*0.8, vTranslational, false);
        
        // BUTTONS
        if (controllerDriver.getRawButtonPressed(11))
            m_ball.resetScrew();
        if (controllerDriver.getRawButtonPressed(Constants.bPositionControl)) {
            m_wheel.positionControl();
        }
        if (controllerDriver.getRawButtonPressed(Constants.bRotationControl)) {
            m_wheel.rotationControl();
        }
        if (controllerDriver.getRawButtonPressed(Constants.bIntakeToggle)) {
            m_ball.toggleIntake();//Turns intake on or off
        }
        if (controllerDriver.getRawButtonPressed(Constants.bSpitOut)) {
            m_ball.toggleSpit();//Turns reverse mode on or off
        }
        if (controllerDriver.getRawButtonPressed(Constants.bToggleWheel)) {
            m_wheel.stopWheel();//Stops position or rotational regardless of current status
        }
        if (controllerShooter.getRawButtonPressed(3)) {
            m_ball.fullShooter();//Shoots full power
        }
        if (controllerShooter.getRawButtonPressed(2)) {
            m_ball.killShooter();//Stops shooter regardless of status
        }
        if (controllerShooter.getRawButtonPressed(1)) {
            drivingStatus = "Shooting";//Switches to shooting mode, controlled in main method
            m_ball.killIntake();
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);
        }
        if (controllerShooter.getRawButton(6) || controllerShooter.getRawButton(7)){
            drivingStatus = "Climbing";
            m_ball.killIntake();
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        }
        if (controllerDriver.getRawButtonPressed(Constants.bDriving)){
            if (!drivingStatus.equals("Driving")) m_ball.toggleIntake();
            drivingStatus = "Driving";
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        }
        if (controllerDriver.getRawButtonPressed(12)){//Change 42 to an actual number once you figure out what button
            drivingStatus = "Loading";
            NetworkTableInstance.getDefault().getTable("limeLight").getEntry("ledMode").setNumber(3);
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(1);
            loadingStationLimeLight();
            m_ball.reviveIntake();//I know this could be redundant but it takes up negliglbe processing power and elimantes stupid mistake

        }
    }

    private double getDistance() {//Returns distance from front of the robot to wall
        return (counter.getPeriod() * 100000 / 2.54)-Constants.LidarError;
        // getPeriod returns cm / µs, then --> sec --> in
    }

    public void Update_Limelight_Tracking() {
        // will have to consider the following for pipelines
        /// NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(<val>);
        double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        /*if (tv==1) {
            m_drive.arcadeDrive(-tx/29, 0, false);
            System.out.println(-tx/29);
        } else {
            m_drive.arcadeDrive(0.5, 0);
        }
        if (Math.abs(tx) < 5 && tv==1) {
            aligning = false;
        } else { aligning = true; }
        System.out.println(tx);*/
        if (tv==1){
            m_drive.arcadeDrive(-0.54*Math.atan(0.5*tx), 0); //0.64*Math.atan(0.2*ty));
        }else{
            m_drive.arcadeDrive(.5,0.0, false);
        }if (tx < 5 && tx > -5){
            aligning = true;
        }else{
            aligning = false;
        }
  }
  public void loadingStationLimeLight(){
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        if (tv==1)
            m_drive.arcadeDrive(0.64*Math.atan(0.2*tx), 0.64*Math.atan(0.2*ty));
        else
            m_drive.arcadeDrive(0.0,.5);
  }
}
