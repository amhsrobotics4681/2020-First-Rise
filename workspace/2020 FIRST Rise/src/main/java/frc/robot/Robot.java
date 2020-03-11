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
    private Joystick controllerDriver, controllerShooter;
    private Victor m_left, m_right;
    private DifferentialDrive m_drive;
    private double vTranslational, vRotational;
    private String drivingStatus, autoStrategy;
    private SendableChooser<String> m_chooser = new SendableChooser<>();
    private int autoTimer;
    private double integral, error,setpoint, derivative, previousError = 0;
    private boolean aligning, isShooting;

    private Index m_index;
    private Intake m_intake;
    private Shooter m_shooter;
    private Screw m_screw;
    private Climber m_climber;
    private Wheel m_wheel;
    private Counter counter;
    private CameraServer m_cameraServer;
    private ADIS16470_IMU m_gyro;

    @Override
    public void robotInit() {
        controllerDriver = new Joystick(0);
        controllerShooter = new Joystick(1);
        m_climber = new Climber();
        m_climber.climberInit();
        m_wheel = new Wheel();
        m_wheel.wheelInit();
        m_index = new Index();
        m_index.indexInit();
        m_intake = new Intake();
        m_intake.intakeInit();
        m_shooter = new Shooter();
        m_shooter.shooterInit();
        m_screw = new Screw();
        m_screw.screwInit();
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
        aligning = true;
        m_gyro.setYawAxis(IMUAxis.kY);
        m_intake.reviveIntake();
        autoStrategy = m_chooser.getSelected();
        //Urus: Shoot 3 balls then drive forward off the line (Works in all positions)
        //Diablo: Shoot 3 balls, turn around, then drve forwards off the line (works in all positions)
        //Aventador: Shoot 3 balls, turn around, drive to collect 3 balls in trench (works on right side) (Does not require Limelight)
        //Veneno: Shoots 3 balls, turns around, drive to collect 3 balls in trench and then shoots (right side) (requires limelight)
    }

    @Override
    public void autonomousPeriodic() {
        m_intake.mainMethod();
        m_shooter.mainMethod();
        m_index.mainMethod(m_intake.getIndexSpinning(), m_shooter.getIndexSpinning());
        autoTimer++;
        if (autoTimer==1)
            m_shooter.resetShooter();
        if (autoTimer < 200)
            m_drive.arcadeDrive(0, 0);
        
        switch (autoStrategy) {
            case "Veneno":
                if (autoTimer > 640) {
                    if (m_gyro.getAngle() < 20) {
                        Update_Limelight_Tracking();
                    } else {
                        m_drive.arcadeDrive(1.0, 0);
                    }
                    if (!aligning)
                        m_shooter.resetShooter();
                }
            case "Aventador":
                if (autoTimer > 400 && autoTimer < 600) {
                    if (m_gyro.getAngle() < 190) {
                        m_drive.arcadeDrive(-0.7, 0);
                    } else {
                        m_drive.arcadeDrive(0, -0.45);
                    }
                }
            case "Diablo":
                if (autoTimer > 250 && autoTimer < 400) {
                    if ((m_gyro.getAngle()) < 180) {
                        m_drive.arcadeDrive(-0.7, 0);
                    } else {
                        m_drive.arcadeDrive(0, -1.0);
                    }
                }
                break;
            case "Urus":
            default:
                if (autoTimer > 200 && autoTimer < 350)
                    m_drive.arcadeDrive(0, -0.7);
                break;
        }
    } 
    
    @Override
    public void teleopInit() {
        m_intake.reviveIntake();
    }

    @Override
    public void teleopPeriodic() {
        //System.out.println("Ball Count: " + m_ball.ballCount()+", Distance: " + (int) getDistance() + ", Color: " + m_wheel.getColor());
        m_intake.mainMethod();
        m_shooter.mainMethod();
        m_index.mainMethod(m_intake.getIndexSpinning(), m_shooter.getIndexSpinning());//Indexer only turns off if shooter and intake dont need it
        m_wheel.mainMethod();
        m_climber.mainMethod();

        // CONTROLS
        if (drivingStatus.equals("Driving")) {//Incramental Acceleration to prevent falling over
            m_shooter.killShooter();
            isShooting = false;
            m_screw.screwSpeed(-controllerDriver.getRawAxis(3));
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
            m_screw.convertElevation((int) getDistance());//Method converts distance to target into required angle of screw
            m_screw.adjustScrew();//Actually moves the screw
            if (!aligning && m_screw.screwAtElevation) {
                // yeah, forget setter/getter functions
                if (isShooting == false){
                    m_shooter.resetShooter();
                    isShooting = true;
                }
                System.out.println("Index should be spinning from shooter: " + m_shooter.getIndexSpinning());
                m_intake.resetBallCount();
            }
        } else if (drivingStatus.equals("Climbing")) {//Controls shift to other remote and everything is dialed down to half power
            vTranslational = (controllerShooter.getRawAxis(1)/2);
            vRotational = (controllerShooter.getRawAxis(0)/2);
            if (controllerShooter.getRawButton(6)) {
                m_climber.extending();//Brings arms up
            } else if (controllerShooter.getRawButton(7)) {
                m_climber.contracting();//Pulls arms back down, which pulls the robot up if it is hooked upon the bar
            } else {
                m_climber.stop();
            }
        }
        if (drivingStatus.equals("Climbing") || drivingStatus.equals("Driving"))
            m_drive.arcadeDrive(-vRotational*0.8, vTranslational, false);//Drives the robot based on whatever input by either driving/shooting/climbing mode
        
        // BUTTONS
        if (controllerDriver.getRawButtonPressed(11))
            m_screw.resetScrew();//Sets whatever current position the screw is as 0 on encoder (for testing purposes only)

        if (controllerDriver.getRawButtonPressed(Constants.bPositionControl)) {
            m_wheel.positionControl();//Color wheel go to set position
        }
        if (controllerDriver.getRawButtonPressed(Constants.bRotationControl)) {
            m_wheel.rotationControl();//Color Wheel spin 3-5 times
        }
        if (controllerDriver.getRawButtonPressed(Constants.bIntakeToggle)) {
            System.out.println("Intake Toggle");
            m_intake.toggleIntake();//Turns intake on or off
        }   
        if (controllerDriver.getRawButtonPressed(Constants.bSpitOut)) {
            m_index.toggleSpit();//Turns reverse mode on or off
            m_intake.toggleSpit();
        }
        if (controllerDriver.getRawButtonPressed(Constants.bToggleWheel)) {
            m_wheel.stopWheel();//Stops position or rotational regardless of current status
        }
        if (controllerShooter.getRawButtonPressed(3)) {
            m_shooter.fullShooter();//Shoots full power
        }
        if (controllerShooter.getRawButtonPressed(2)) {
            m_shooter.killShooter();//Stops shooter regardless of status
        }
        if (controllerShooter.getRawButtonPressed(1)) {
            drivingStatus = "Shooting";//Switches to shooting mode, controlled in main method
            m_intake.killIntake();
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);//Turns LED light on for Limelight
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);//Makes sure pipeline is set to shooting target
        }
        if (controllerShooter.getRawButton(6) || controllerShooter.getRawButton(7)){
            drivingStatus = "Climbing";
            m_intake.killIntake();//Stops intake for climbing
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);//Turns light off. No need for it while climbing
        }
        if (controllerDriver.getRawButtonPressed(Constants.bDriving)){
            if (!drivingStatus.equals("Driving")) m_intake.toggleIntake();
            drivingStatus = "Driving";
            m_index.killIndex();
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        }
        if (controllerDriver.getRawButtonPressed(12)){//Method has not currently been tested
            drivingStatus = "Loading";
            NetworkTableInstance.getDefault().getTable("limeLight").getEntry("ledMode").setNumber(3);//Turns lights on
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(1);//Switches to targeting of loading station
            loadingStationLimeLight();
            m_intake.reviveIntake();//I know this could be redundant but it takes up negliglbe processing power and elimantes stupid mistake

        }
    }

    private double getDistance() {//Returns distance from front of the robot to wall
        return (counter.getPeriod() * 100000 / 2.54)-Constants.LidarError;
        // getPeriod returns cm / µs, then --> sec --> in
    }

    public void Update_Limelight_Tracking() {
        // will have to consider the following for pipelines
        /// NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(<val>);
        double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);//Change in x from cross hair
        if (tv == 1){
            m_drive.arcadeDrive(PID()*0.03, 0);
        }
        else {
            m_drive.arcadeDrive(.5,0);
        }
        if (Math.abs(tx) < 3.5){
            aligning = false;
        }
        else 
            aligning = true;
                
    }
  public void loadingStationLimeLight(){
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);//Change in x from cross hair
    double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);//Change in y from cross hair
    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);//if tv == 0, no target found, if tv == 1, target found
    if (tv==1)
        m_drive.arcadeDrive(0.64*Math.atan(0.2*tx), 0.64*Math.atan(0.2*ty));
    else
        m_drive.arcadeDrive(0.0,.5);
  }
  public double PID(){
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    error = (setpoint - tx)*.4;
    integral += error*.02;
    derivative = (previousError - error)/.1;
    previousError = error;
    //FSystem.out.println("Error " + error + " Integral " + integral);
    return (error + integral + derivative);
  }
}
