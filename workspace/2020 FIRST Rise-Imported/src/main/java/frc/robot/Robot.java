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

public class Robot extends TimedRobot {
    private Joystick controllerDriver, controllerShooter;
    private Victor m_left, m_right;
    private DifferentialDrive m_drive;
    private double vTranslational, vRotational = 0;
    private String drivingStatus, autoStrategy;
    private SendableChooser<String> m_chooser = new SendableChooser<>();
    private int timer;
    private double integral, error,setpoint, derivative, previousError = 0;
    private boolean aligned;

    private Index m_index;
    private Intake m_intake;
    private Shooter m_shooter;
    private Screw m_screw;
    private Climber m_climber;
    private Wheel m_wheel;
    private Counter counter;
    private ADIS16470_IMU m_gyro;
    private Limelight m_limelight;

    @Override
    public void robotInit() {
        controllerDriver = new Joystick(0);
        controllerShooter = new Joystick(1);
        m_left = new Victor(Constants.PWM_TreadsLeft);
        m_right = new Victor(Constants.PWM_TreadsRight);
        m_right.setInverted(false);
        m_left.setInverted(true);
        m_drive = new DifferentialDrive(m_left, m_right);

        m_climber = new Climber();
        m_wheel = new Wheel();
        m_index = new Index();
        m_intake = new Intake();
        m_shooter = new Shooter();
        m_screw = new Screw();
        m_gyro = new ADIS16470_IMU();
        m_limelight = new Limelight();
        
        counter = new Counter(Constants.DIO_LIDAR);
        counter.setMaxPeriod(1.0);
        counter.setSemiPeriodMode(true);
        counter.reset();
        m_chooser.setDefaultOption("Shoot, Fd", "Urus");
        m_chooser.addOption("Shoot, Turn, Fd", "Diablo");
        m_chooser.addOption("Shoot, Collect", "Aventador");
        m_chooser.addOption("Shoot, Collect, Shoot", "Veneno");
        SmartDashboard.putData("Auto Choices", m_chooser);
        CameraServer.getInstance().startAutomaticCapture("Shooting Camera", 0);
        CameraServer.getInstance().startAutomaticCapture("Collecting Camera", 1);
    }

    @Override
    public void autonomousInit() {
        timer = 0;
        aligned = false;
        m_gyro.setYawAxis(IMUAxis.kY);
        autoStrategy = m_chooser.getSelected();
        m_drive.setSafetyEnabled(false);
        //Urus: Shoot 3 balls then drive forward off the line (Works in all positions)
        //Diablo: Shoot 3 balls, turn around, then drve forwards off the line (works in all positions)
        //Aventador: Shoot 3 balls, turn around, drive to collect 3 balls in trench (works on right side) (Does not require Limelight)
        //Veneno: Shoots 3 balls, turns around, drive to collect 3 balls in trench and then shoots (right side) (requires limelight)
    }

    @Override
    public void autonomousPeriodic() {
        timer++;
        m_index.mainMethod();
        m_shooter.standardShooting();
        if (timer <= 220)
            m_drive.arcadeDrive(0, 0);

        switch (autoStrategy) {
            case "Veneno":
                if (timer > 640) {
                    limelight_Shooting();
                    if (aligned) {
                        m_shooter.standardShooting();
                        m_index.setEjecting(m_shooter.getEjecting());
                    }
                    break;
                }
            case "Aventador":
                if (timer > 440) {
                    if (timer > 610) {
                        m_drive.arcadeDrive(0.6, 0);
                    } else if (m_gyro.getAngle() < 190) {
                        m_drive.arcadeDrive(-0.7, 0);
                    } else {
                        m_drive.arcadeDrive(0, -0.5);
                        m_intake.setIntake(true);
                    }
                    break;
                }
            case "Diablo":
                if (timer > 220 && timer < 440) {
                    if ((m_gyro.getAngle()) < 180) {
                        m_drive.arcadeDrive(-0.7, 0);
                    } else {
                        m_drive.arcadeDrive(0, -1.0);
                    }
                } else {
                    m_drive.arcadeDrive(0, 0);
                }
                break;
            case "Urus":
            default:
                if (timer > 220 && timer < 350)
                    m_drive.arcadeDrive(0, -0.7);
                else
                    m_drive.arcadeDrive(0, 0);
                break;
        }
    } 
    
    @Override
    public void teleopInit() {
        m_intake.setIntake(true);
        drivingStatus = "Driving";
        m_limelight.setLED(false);
        m_index.setEjecting(false);
    }

    @Override
    public void teleopPeriodic() {
        m_index.mainMethod();
        m_wheel.mainMethod();
        m_climber.mainMethod();

        // CONTROLS
        if (drivingStatus.equals("Driving")) {//Incramental Acceleration to prevent falling over
            if(vTranslational < controllerDriver.getRawAxis(1))
                vTranslational += Constants.kSpeedCurve;
            if(vTranslational > controllerDriver.getRawAxis(1))
                vTranslational -= Constants.kSpeedCurve;
            if(vRotational < controllerDriver.getRawAxis(2))
                vRotational += Constants.kSpeedCurve;
            if(vRotational > controllerDriver.getRawAxis(2))
                vRotational -= Constants.kSpeedCurve;
        }
        else if (drivingStatus.equals("Shooting")) {//Starts autodriving, adjusts screw
            limelight_Shooting();
            m_screw.setSpeed(-controllerShooter.getRawAxis(1)); // until numbers testing & regression formula
            if (aligned && m_screw.screwAtElevation) {
                m_limelight.setLED(false);
                m_shooter.standardShooting();
                m_index.setEjecting(m_shooter.getEjecting());
            }
        }
        else if (drivingStatus.equals("Full Shooting")) {//Starts autodriving, adjusts screw
            limelight_Shooting();
            m_screw.setSpeed(-controllerShooter.getRawAxis(1));
            if (aligned && m_screw.screwAtElevation) {
                m_limelight.setLED(false);
                m_shooter.fullShooting();
                m_index.setEjecting(m_shooter.getEjecting());
            }
        }
        else if (drivingStatus.equals("Climbing")) {//Controls shift to other remote and everything is dialed down to half power
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
        else if (drivingStatus.equals("Loading")) {
            System.out.println("You're fired from the drive team");
        }

        if (drivingStatus.equals("Driving") || drivingStatus.equals("Climbing"))
            m_drive.arcadeDrive(-vRotational*0.8, vTranslational, false);
        
        // BUTTONS
        if (controllerDriver.getRawButtonPressed(Constants.bResetScrew))
            m_screw.resetScrew(); // set current screw position as 0 on encoder (for testing purposes only)

        if (controllerDriver.getRawButtonPressed(Constants.bPositionControl)) {
            m_wheel.positionControl();//Color wheel go to set position
        }
        if (controllerDriver.getRawButtonPressed(Constants.bRotationControl)) {
            m_wheel.rotationControl();//Color Wheel spin 3-5 times
        }
        if (controllerDriver.getRawButtonPressed(Constants.bStopWheel)) {
            m_wheel.stopWheel();
        }
        if (controllerDriver.getRawButtonPressed(Constants.bIntakeToggle)) {
            m_intake.setIntake(!m_intake.getIntake());
            m_index.setSpitting(false);
        }   
        if (controllerDriver.getRawButtonPressed(Constants.bSpitToggle)) {
            m_index.setSpitting(!m_index.getSpitting());
            m_intake.setSpitting(!m_intake.getSpitting());
        }
        if (controllerShooter.getRawButtonPressed(2)) {
            m_shooter.killShooter();//Stops shooter regardless of status
        }
        
        // MODE SWITCHING
        if (controllerShooter.getRawButtonPressed(3)) {
            drivingStatus = "Full Shooting";
            m_intake.setIntake(false);
            m_limelight.setLED(true);
            m_limelight.setPipeline(0);
            m_shooter.resetTimer();
        }
        if (controllerShooter.getRawButtonPressed(1)) {
            drivingStatus = "Shooting";//Switches to shooting mode, controlled in main method
            m_intake.setIntake(false);
            m_limelight.setLED(true);
            m_limelight.setPipeline(0);
            m_shooter.resetTimer();
        }
        if (controllerShooter.getRawButton(6) || controllerShooter.getRawButton(7)){
            drivingStatus = "Climbing";
            m_intake.setIntake(false);//Stops intake for climbing
            m_limelight.setLED(false);
        }
        if (controllerDriver.getRawButtonPressed(Constants.bDriving)) {
            drivingStatus = "Driving";
            m_intake.setIntake(true);
            m_index.setEjecting(false);
            m_limelight.setLED(false);
        }
        if (controllerDriver.getRawButtonPressed(Constants.bLoading)){//Method has not currently been tested
            drivingStatus = "Loading";
            m_limelight.setLED(true);
            limelight_Loading();
            m_intake.setIntake(true);
            m_limelight.setPipeline(1); // which means, don't press button or code breaks :-)
        }
    }

    private double getDistance() {//Returns distance from front of the robot to wall
        return (counter.getPeriod() * 100000 / 2.54)-Constants.LidarError;
        // getPeriod returns cm / µs, then --> sec --> in
    }

    public void limelight_Shooting() {
        if (m_limelight.hasValidTarget()) {
            m_drive.arcadeDrive(PID()*0.03, 0);
        } else {
            m_drive.arcadeDrive(.5,0); // when in doubt, aim right
        }
        aligned = m_limelight.isAligned();                
    }

    public void limelight_Loading() { // will align us in 2 ft semicircle around vision target
        m_drive.arcadeDrive(PID()*0.03, (getDistance()-24)/24);
    }

    public double PID(){
        error = (setpoint - m_limelight.getX())*.4;
        integral += error*.02;
        derivative = (previousError - error)/.1;
        previousError = error;
        //FSystem.out.println("Error " + error + " Integral " + integral);
        return (error + integral + derivative);
    }
}