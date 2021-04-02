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
    private boolean aligned, currentlyShooting;

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
        m_chooser.setDefaultOption("Barrel Racing Path", "Barrel");
        m_chooser.addOption("Slalom path", "Slalom");
        m_chooser.addOption("Galactic Search B - Red", "BRed");
        m_chooser.addOption("Galactic Search B - Blue", "BBlue");
        m_chooser.addOption("Galactic Search A - Red (Green)", "ARed");
        m_chooser.addOption("Galactic Search A - Blue (White)", "ABlue");
        SmartDashboard.putData("Auto Choices", m_chooser);
        CameraServer.getInstance().startAutomaticCapture("Shooting Camera", 0);
        CameraServer.getInstance().startAutomaticCapture("Collecting Camera", 1);
    }

    @Override
    public void autonomousInit() {
        timer = 0;
        setStatus("Auto", false);

        m_gyro.setYawAxis(IMUAxis.kY);
        m_gyro.reset();
        autoStrategy = m_chooser.getSelected();
        m_drive.setSafetyEnabled(false);
    }

    @Override
    public void autonomousPeriodic() {

        //Galactic Search Timer
        timer++;
        System.out.println(timer);
        m_index.mainMethod();
        
        switch(autoStrategy){
            case "BRed":
                // GALACTIC SEARCH LAYOUT PATH B - RED
                if(timer >= 2 && timer < 60){
                    driveCurve(1, vRotational);
                } else if (timer < 260) {
                    driveCurve(0.75, vRotational);
                } else if (timer < 370) {
                    driveCurve(1, vRotational);
                } else {
                    driveCurve(0, vRotational);
                }

                //rotation instructions
                if(timer >= 63 && m_gyro.getAngle() < 15 && timer < 150){ //(timer >= 63 && timer < 80)
                    driveCurve(vTranslational, 1);
                } else if (timer >= 150 && m_gyro.getAngle() > -14 && timer < 255) { //(timer >= 150 && timer < 193)
                    driveCurve(vTranslational, -1);
                } else if (timer >= 255 && m_gyro.getAngle() < -30 && timer < 300){ //(timer >= 255 && timer < 277)
                    driveCurve(vTranslational, 1);
                } else {
                    driveCurve(vTranslational, 0);
                }
            break;

            case "BBlue":
                //GALACTIC SEARCH LAYOUT PATH B - BLUE
                if (timer < 130) {
                    driveCurve(1, vRotational);
                } else if (timer < 370) {
                    driveCurve(0.75, vRotational);
                } else if (timer < 420) {
                    driveCurve(1, vRotational);
                } else {
                    driveCurve(0,vRotational);
                }

                //rotation instructions
                if (timer >= 130 && m_gyro.getAngle() > -15 && timer < 200){ //(timer >= 63 && timer < 80)
                    driveCurve(vTranslational, -1);
                } else if (timer >= 240 && m_gyro.getAngle() < 15 && timer < 300) { //(timer >= 150 && timer < 193)
                    driveCurve(vTranslational, 1);
                } else if (timer >= 360 && m_gyro.getAngle() > 30 && timer < 400){ //(timer >= 255 && timer < 277)
                    driveCurve(vTranslational, -1);
                } else {
                    driveCurve(vTranslational, 0);
                }
                break;

            case "ARed":
                //GALACTIC SEARCH LAYOUT PATH A - RED
                if (timer < 55) {
                    driveCurve(1, vRotational);
                } else if (timer < 135) {
                    driveCurve(0.75, vRotational);
                } else {
                    driveCurve(0,vRotational);
                }

                //rotation instructions
                if (timer >= 55 && m_gyro.getAngle() < 4 && timer < 135){ 
                    driveCurve(vTranslational, 1);
                } else {
                    driveCurve(vTranslational, 0);
                }
                break;

            case "Barrel":
                //BARREL RACING PATH
                if (timer < 1000) {
                    driveCurve(1, vRotational);
                } else {
                    driveCurve(0,vRotational);
                }

                //rotation instructions
                if (m_gyro.getAngle() > -1 && timer < 50){ 
                    driveCurve(vTranslational, -0.25);
                } else if (timer >= 118 && m_gyro.getAngle() < 340 && timer < 415){ 
                    driveCurve(vTranslational, 0.60);
                } else if (timer >= 405 && m_gyro.getAngle() > 60 && timer < 685){ 
                    driveCurve(vTranslational, -0.65);
                } else if (timer >= 685 && m_gyro.getAngle() > -165 && timer < 1000){ 
                        driveCurve(vTranslational, -0.65);
                } else {
                    driveCurve(vTranslational, 0);
                }
                break;
            case "Slalom":
                //SLALOM RACING PATH
                if(timer < 0){
                    driveCurve(1, vRotational);
                } else {
                    driveCurve(0, vRotational);
                }

                //rotation instructions
                if (m_gyro.getAngle() > 0 && timer < 0){ 
                    driveCurve(vTranslational, -0.0);
                } else if (timer >= 0 && m_gyro.getAngle() < 0 && timer < 0){ 
                    driveCurve(vTranslational, 0.0);
                } else if (timer >= 0 && m_gyro.getAngle() < 0 && timer < 0){ 
                    driveCurve(vTranslational, 0.0);
                } else if (timer >= 0 && m_gyro.getAngle() > 0 && timer < 0){ 
                        driveCurve(vTranslational, -0.0);
                } else if (timer >= 0 && m_gyro.getAngle() < 0 && timer < 0){ 
                    driveCurve(vTranslational, 0.0);
                } else if (timer >= 0 && m_gyro.getAngle() < 0 && timer < 0){ 
                    driveCurve(vTranslational, 0.0);
                } else if (timer >= 0 && m_gyro.getAngle() > 0 && timer < 0){ 
                    driveCurve(vTranslational, -0.0);
                } else {
                    driveCurve(vTranslational, 0);
                }

            default:
        }

        //Galactic Search Drive
        m_drive.arcadeDrive(-vRotational, -vTranslational, false);
    } 
    
    @Override
    public void teleopInit() {
        setStatus("Driving", false); // driving mode and turn off LL
    }

    @Override
    public void teleopPeriodic() {
        //System.out.println(m_limelight.hasValidTarget());
        m_index.mainMethod();
        m_wheel.mainMethod();
        m_climber.mainMethod();

        // CONTROLS
        if (drivingStatus.equals("Driving")) {//Incramental Acceleration to prevent falling over
            driveCurve(controllerDriver.getRawAxis(1), controllerDriver.getRawAxis(2));
        } else if (drivingStatus.equals("Manual Shooting")) {
            vRotational = controllerShooter.getRawAxis(0);
            m_screw.setSpeed(-controllerShooter.getRawAxis(1));
            if(controllerShooter.getRawButtonPressed(1))
                aligned = true;
            if (aligned) {
                m_shooter.startShooter(false);
                m_index.setEjecting(m_shooter.getEjecting());
            }
        // auto-drives and auto-screws
        } else if (drivingStatus.equals("Limelight Shooting") || drivingStatus.equals("Full Shooting")) {
            /*potential issue: if screwAtElevation is too sensitive this condition could be toggled a couple hundred times a second
            will not cause problems for the time being, but we'll have to keep an eye out if we make screwAtElevation automatic*/
            limelight_Shooting();
            m_screw.setSpeed(-controllerShooter.getRawAxis(1)); // until numbers testing & regression formula

            /*for some reason the robot thinks we are always aligned
            will control manually until fixed
            if (aligned && m_screw.screwAtElevation) 
                currentlyShooting = true;*/
            if(controllerShooter.getRawButtonPressed(1))
                currentlyShooting = true;
            if(currentlyShooting){
                m_shooter.startShooter(drivingStatus.equals("Full Shooting"));
                m_index.setEjecting(m_shooter.getEjecting());
            }
        }
        else if (drivingStatus.equals("Climbing")) {//Controls shift to other remote and everything is dialed down to half power
            vTranslational = (controllerShooter.getRawAxis(1)/2);
            vRotational = (controllerShooter.getRawAxis(0)/2);
            if (controllerShooter.getRawButton(6)) {
                m_climber.extending(); // Brings arms up
            } else if (controllerShooter.getRawButton(7)) {
                m_climber.contracting(); // Pulls arms back down, which pulls the robot up if it is hooked upon the bar
            } else {
                m_climber.stop();
            }
        }
        else if (drivingStatus.equals("Loading")) {
            System.out.println("You're fired from the drive team");
            // limelight_Loading();
        }

        // DRIVE SPEED LOGIC
        if (drivingStatus.equals("Driving") || drivingStatus.equals("Climbing"))
            m_drive.arcadeDrive(-vRotational*0.8 + 0.04, vTranslational, false);
        else if (drivingStatus.equals("Manual Shooting"))
            m_drive.arcadeDrive(-vRotational*0.5, 0, false);
        
        // BUTTONS
        if (controllerDriver.getRawButtonPressed(Constants.bResetScrew))
            m_screw.resetScrew(); // set current screw position as 0 on encoder (for testing purposes only)

        //Something is wrong with the wheel: It's not connected for some reason
        //We'll comment this out to prevent a null pointer until we can fix a problem
        /*if (controllerDriver.getRawButtonPressed(Constants.bPositionControl)) {
            m_wheel.positionControl();//Color wheel go to set position
        }
        if (controllerDriver.getRawButtonPressed(Constants.bRotationControl)) {
            m_wheel.rotationControl();//Color Wheel spin 3-5 times
        }
        if (controllerDriver.getRawButtonPressed(Constants.bStopWheel)) {
            m_wheel.stopWheel();
        }*/
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
        
        //FOR TESTING- limelight toggles
        if(controllerShooter.getRawButton(8))
            m_limelight.setLED(!m_limelight.getLED());

        // MODE SWITCHING - logic is in teleop-CONTROLS
        if(controllerShooter.getRawButtonPressed(3)) {
            setStatus("Manual Shooting", false);
        }
        /* Removed for saftey, will re-implement later
        else if (controllerShooter.getRawButtonPressed(5)) {
            setStatus("Full Shooting", true);
        }*/
        else if (controllerShooter.getRawButtonPressed(4)) {
            setStatus("Limelight Shooting", true);
        }
        else if (controllerShooter.getRawButton(6) || controllerShooter.getRawButton(7)) {
            setStatus("Climbing", false);
        }
        else if (controllerDriver.getRawButtonPressed(Constants.bDriving)) {
            setStatus("Driving", false);
        }
        /* Untested and liable to break. Kindly don't use.
        else if (controllerDriver.getRawButtonPressed(Constants.bLoading)) {
            setStatus("Loading", true);
        }*/

        //limelight LED testing
        //m_limelight.setLED(controllerDriver.getRawButton(7));
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
        aligned = aligned || m_limelight.isAligned(); // if we've aligned, we're aligned til status reset
    }

    public void limelight_Loading() { // will align us in 2 ft semicircle around vision target
        m_drive.arcadeDrive(PID()*0.03, (getDistance()-24)/24);
    }

    private void driveCurve(double speed, double rotation){
        if(vTranslational < speed)
            vTranslational += Constants.kSpeedCurve;
        else if (vTranslational > speed)
            vTranslational -= Constants.kSpeedCurve;
        if(vRotational < rotation)
            vRotational += Constants.kSpeedCurve;
        else if(vRotational > rotation)
            vRotational -= Constants.kSpeedCurve;
    }

    private void setStatus(String status, boolean limelightLED) {
        drivingStatus = status;
        m_limelight.setLED(limelightLED);
        aligned = currentlyShooting = false;
        m_shooter.resetTimer();
        switch (status) {
            case "Full Shooting": case "Limelight Shooting":
                m_limelight.setPipeline(0);
            case "Manual Shooting":
                m_intake.setIntake(false);
                break;
            case "Loading":
                m_limelight.setPipeline(1);
            case "Driving":
                m_intake.setIntake(true);
            case "Climbing": case "Auto":
                m_index.setEjecting(false);
                break;
        }
        if (status.equals("Climbing") || status.equals("Auto")) // to prevent intake motor jerking
            m_intake.setIntake(false);
    }

    public double PID(){
        error = (setpoint - m_limelight.getX())*.4;
        integral += error*.02;
        derivative = (previousError - error)/.1;
        previousError = error;
        return (error + integral + derivative);
    }

    @Override
    public void testPeriodic() {
        System.out.println(m_gyro.getAngle());
    }

    @Override
    public void disabledInit(){
        m_limelight.setLED(false);
    }
}