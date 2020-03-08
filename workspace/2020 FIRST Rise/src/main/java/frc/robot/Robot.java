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
import edu.wpi.first.wpilibj.DriverStation;
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
    private boolean targetDetected;
    private double limeLightDriveCommand;
    private double limeLightSteerCommand;

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
        targetDetected = false;
    }

    @Override
    public void autonomousInit() {
        autoTimer = 0;
        autoShoot = false;
        aligning = true;
        m_gyro.setYawAxis(IMUAxis.kY);
        m_ball.toggleIntake();
        autoStrategy = m_chooser.getSelected();
        //Urus: Shoot 3 balls then drive forward off the line (Works in all positions)
        //Diablo: Shoot 3 balls, turn around, then drve forwards off the line (works in all positions)
        //Aventador: Shoot 3 balls, turn around, drive to collect 3 balls in trench (works on right side) (Does not require Limelight)
        //Veneno: Shoots 3 balls, turns around, drive to collect 3 balls in trench and then shoots (right side) (requires limelight)
    }

    @Override
    public void autonomousPeriodic() {
        m_ball.mainMethod();
        autoTimer++;
        System.out.println(autoTimer);
        if (!autoShoot) { 
            m_ball.resetShooter();
            autoShoot = true;
        }
        
        if (autoStrategy.equals("Urus")){
            if (autoTimer > 200 && autoTimer < 350) {
                m_drive.arcadeDrive(0, -0.7);
            } else {
                m_drive.arcadeDrive(0,0);
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
        } else if (autoStrategy.equals("Veneno")){
            if (autoTimer > 670) {
                if (m_gyro.getAngle() > 20) {
                    m_drive.arcadeDrive(1.0, 0);
                } else {
                    align();
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
        m_wheel.mainMethod();
        m_climber.mainMethod();

        // CONTROLS
        if (drivingStatus.equals("Driving")) {
            m_ball.screwSpeed(-1*controllerShooter.getRawAxis(1));
            if(vTranslational < controllerDriver.getRawAxis(1))
                vTranslational += Constants.kSpeedCurve;
            if(vTranslational > controllerDriver.getRawAxis(1))
                vTranslational -= Constants.kSpeedCurve;
            if(vRotational < controllerDriver.getRawAxis(2))
                vRotational += Constants.kSpeedCurve;
            if(vRotational > controllerDriver.getRawAxis(2))
                vRotational -= Constants.kSpeedCurve;
        } else if (drivingStatus.equals("Shooting")) {
            if (Math.abs(controllerShooter.getRawAxis(1)) > .2){
                m_ball.screwSpeed(-1*controllerShooter.getRawAxis(1));
            }
            else{
                m_ball.screwSpeed(0);
            }
            vTranslational = 0;
            vRotational = (controllerShooter.getRawAxis(0)/2); 
        } else if (drivingStatus.equals("Climbing")) {
            vTranslational = (controllerShooter.getRawAxis(1)/2);
            vRotational = (controllerShooter.getRawAxis(0)/2);
            if (controllerShooter.getRawButton(6)) {
                m_climber.extending();
                //System.out.println("Extending");
            } else if (controllerShooter.getRawButton(7)) {
                m_climber.contracting();
                //System.out.println("Contracting");
            } else {
                m_climber.stop();
            }
        }
        m_drive.arcadeDrive(-vRotational*0.8, vTranslational, false);
        // ^ false means inputs aren't squared. may need to get used to new drive style
        // basically arcadeDrive(0, -0,5) gives 25% power, but (0, -0.5, false) gives 50% power
        
        // BUTTONS
        if (controllerDriver.getRawButtonPressed(Constants.bPositionControl)){
            m_wheel.positionControl();
        }
        if (controllerDriver.getRawButtonPressed(Constants.bRotationControl)){
            m_wheel.rotationControl();
        }
        if (controllerDriver.getRawButtonPressed(Constants.bIntakeToggle)){
            m_ball.toggleIntake();
        }
        if (controllerDriver.getRawButtonPressed(Constants.bSpitOut)) {
            m_ball.toggleSpit();
        }
        if (controllerShooter.getRawButtonPressed(1)){
            m_ball.resetShooter();
        }
        if (controllerDriver.getRawButtonPressed(Constants.bToggleWheel)){
            m_wheel.stopWheel();
        }
        if (controllerShooter.getRawButtonPressed(3)){
            m_ball.fullShooter();
        }
        if (controllerShooter.getRawButtonPressed(2)){
            m_ball.killShooter();
        }
        if (controllerShooter.getRawButtonPressed(11)){
            drivingStatus = "Shooting";
            m_ball.killIntake();
        }
        if (controllerShooter.getRawButtonPressed(10)){
            drivingStatus = "Climbing";
            m_ball.killIntake();
        }
        if (controllerDriver.getRawButtonPressed(Constants.bDriving)){
            drivingStatus = "Driving";
            m_ball.reviveIntake();
        }
        if (controllerDriver.getRawButton(Constants.bAlignRobot)) {
            align();
            if (!aligning)
                m_ball.resetShooter();
        } else {
            aligning = false;
        }
    }

    private double getDistance() {
        return (counter.getPeriod() * 100000 / 2.54)-Constants.LidarError;
        // getPeriod returns cm / µs, then --> sec --> in
    }

    private void align() {
        aligning = true;
        // if location of tape is negative, rotate left; aligning = true;
        // if location of tape is positive, rotate right; aligning = true;
        // else, set aligning to false. we're done
    }
    public void Update_Limelight_Tracking(){
        // These numbers must be tuned for your Robot!  Be careful!
        final double STEER_K = 0.03;                    // how hard to turn toward the target
        final double DRIVE_K = 0.26;                    // how hard to drive fwd toward the target
        final double DESIRED_TARGET_AREA = 13.0;        // Area of the target when the robot reaches the wall
        final double MAX_DRIVE = 0.7;                   // Simple speed limit so we don't drive too fast
        double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        if (tv < 1.0){
          targetDetected = false;
          limeLightDriveCommand = 0.0;
          limeLightSteerCommand = 0.0;
          return;
        }
        //Everything below only happens if there is a target in view
        targetDetected = true;
        // Start with proportional steering
        double steer_cmd = tx * STEER_K;
        limeLightSteerCommand = steer_cmd;
        // try to drive forward until the target area reaches our desired area
        double drive_cmd = (DESIRED_TARGET_AREA - ta) * DRIVE_K;
        // don't let the robot drive too fast into the goal
        if (drive_cmd > MAX_DRIVE)
        {
          drive_cmd = MAX_DRIVE;
        }
        limeLightDriveCommand = drive_cmd;
  }
}
