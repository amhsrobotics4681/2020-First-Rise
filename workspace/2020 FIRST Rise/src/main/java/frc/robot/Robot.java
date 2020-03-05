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
    private ADIS16470_IMU m_gyro;

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
        aligning = true;
        vTranslational = 0;
        vRotational = 0;
        drivingStatus = "Driving";
        m_gyro = new ADIS16470_IMU();
        m_cameraServer.getInstance().startAutomaticCapture("Shooting Camera", 0);
        m_cameraServer.getInstance().startAutomaticCapture("Collecting Camera", 1);
    }

    @Override
    public void autonomousInit() {
        autoTimer = 0;
        autoShoot = false;
        m_gyro.setYawAxis(IMUAxis.kY);
        m_ball.toggleIntake();
        autoStrategy = DriverStation.getInstance().getGameSpecificMessage();
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
                m_drive.arcadeDrive(0,0.7);
            } else {
                m_drive.arcadeDrive(0,0);
            }
        }
        else if (autoStrategy.equals("Diablo")){
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
        }
        /** STRATEGY 3
         * Until timer is 280, don't move. then turn 180
         * and forward until timer is 500
         * Then turn a tiny bit more and forward over balls
         */
        else if (autoStrategy.equals("Aventador")){    
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
        else if (autoStrategy.equals("Veneno")){
            //finish this one, Requires limelight
        }
        else{
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
            vTranslational = 0;
            vRotational = (controllerShooter.getRawAxis(0));
            m_ball.screwSpeed(-1*controllerShooter.getRawAxis(1)); 
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
        m_drive.arcadeDrive(-vRotational*0.8, vTranslational);
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
}
