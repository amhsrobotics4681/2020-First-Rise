/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.cscore.UsbCamera;

public class Robot extends TimedRobot {
    private Joystick controller;
    private Climber m_climber;
    private Wheel m_wheel;
    private BallSystem m_ball;
    private Victor m_left;
    private Victor m_right;
    private DifferentialDrive m_drive;
    private Counter counter;
    private boolean aligning;
    private double vTranslational;
    private double vRotational;
    private int timer;
    private UsbCamera m_cameraServer;

    @Override
    public void robotInit() {
        controller = new Joystick(0);
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
        m_cameraServer = new UsbCamera("Front Camera", 1);
    }

    @Override
    public void autonomousPeriodic() {
        align();
        m_ball.mainMethod();
        if (!aligning) m_ball.resetShooter();
        if (!m_ball.currentlyShooting && getDistance()<180) m_drive.arcadeDrive(-1, 0);
    } 
    
    @Override
    public void teleopInit() {
        m_ball.toggleIntake();
    }

    @Override
    public void teleopPeriodic() {
        System.out.println(getDistance());
        m_ball.mainMethod();
        m_wheel.mainMethod();
        m_ball.screwSpeed(-1*controller.getRawAxis(3));
        align();
        //Controls
        if(vTranslational < controller.getRawAxis(1)){
            vTranslational += Constants.kSpeedCurve;
        }
        if(vTranslational > controller.getRawAxis(1)){
            vTranslational -= Constants.kSpeedCurve;
        }
        if(vRotational < controller.getRawAxis(0)){
            vRotational += Constants.kSpeedCurve;
        }
        if(vRotational > controller.getRawAxis(0)){
            vRotational -= Constants.kSpeedCurve;
        }
        m_drive.arcadeDrive(-vRotational, vTranslational);

        if (controller.getPOV() == 0){
            m_climber.extending();
        } else if (controller.getPOV() == 180){
            m_climber.contracting();
        } else {
            m_climber.stop();
        }
        if (controller.getRawButtonPressed(Constants.bPositionControl)){
            m_wheel.positionControl();
        }
        if (controller.getRawButtonPressed(Constants.bRotationControl)){
            m_wheel.rotationControl();
        }
        if (controller.getRawButtonPressed(Constants.bIntakeToggle)){
            m_ball.toggleIntake();
        }
        if (controller.getRawButtonPressed(Constants.bResetShooter)){
            m_ball.resetShooter();
        }
        if (controller.getRawButtonPressed(Constants.bToggleWheel)){
            m_wheel.toggleWheel();
        }
        if (controller.getRawButtonPressed(Constants.bIndexToggle)){
            m_ball.toggleIndexer();
        }
        if (controller.getRawButtonPressed(Constants.bKillShooter)){
            m_ball.killShooter();
        }
        if (controller.getRawButtonPressed(Constants.bSpitOut))
            m_ball.spit();
        if (controller.getRawButton(Constants.bAlignRobot)) {
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
