/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;

public class Robot extends TimedRobot {
    private Joystick controller;
    private Climber m_climber;
    private Wheel m_wheel;
    private BallSystem m_ball;
    private int timer;
    private double cumulative;
    private Victor m_left;
    private Victor m_right;
    private DifferentialDrive m_drive;
    private Counter counter;
    private boolean aligning;
    private double vTranslational;
    private double vRotational;

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
        m_right.setInverted(true);
        m_left.setInverted(true);
        m_drive = new DifferentialDrive(m_left, m_right);
        counter = new Counter(Constants.DIO_LIDAR);
        counter.setMaxPeriod(1.0);
        counter.setSemiPeriodMode(true);
        counter.reset();
        aligning = true;
        vTranslational = 0;
        vRotational = 0;
    }

    @Override
    public void autonomousPeriodic() {
        align();
        m_ball.mainMethod();
        if (!aligning)
            m_ball.resetShooter();
        if (!m_ball.currentlyShooting && getDistance()<180){
            m_drive.arcadeDrive(-1, 0);
        }
    } 
    
    @Override
    public void teleopInit() {
        m_ball.toggleIntake();
    }

    @Override
    public void teleopPeriodic() {
        timer++;
        cumulative += getDistance();
        if (timer%10==0){
            //System.out.println((Double.toString(cumulative/10)).substring(0,5));
            cumulative = 0;
        }
        m_ball.mainMethod();
        m_wheel.mainMethod();
        m_ball.screwSpeed(-1*controller.getRawAxis(3));
        align();

        //SmartDashboard outputs
        SmartDashboard.putString("Detected Color", m_wheel.getColor());
        SmartDashboard.putNumber("Red", m_wheel.getRed());
        SmartDashboard.putNumber("Green", m_wheel.getGreen());
        SmartDashboard.putNumber("Blue", m_wheel.getBlue());
        SmartDashboard.putNumber("Confidence", m_wheel.getConfidence());
        SmartDashboard.putNumber("Proximity", m_wheel.getProximity());

        //Controls
        if(vTranslational < controller.getRawAxis(1)){
            vTranslational += Constants.kSpeedCurve;
        }
        if(vTranslational > controller.getRawAxis(1)){
            vTranslational -= Constants.kSpeedCurve;
        }
        if(vRotational < controller.getRawAxis(1)){
            vRotational += Constants.kSpeedCurve;
        }
        if(vRotational > controller.getRawAxis(1)){
            vRotational += Constants.kSpeedCurve;
        }
        m_drive.arcadeDrive(vTranslational, vRotational);

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
        if (controller.getRawButtonPressed(Constants.bAlignRobot)){
            startAlign();
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
    }

    private double getDistance() {
        return counter.getPeriod() * 100000 / 2.54;
        // getPeriod returns cm / µs, then --> sec --> in
    }

    //Pending completion of vision project
    private void startAlign() {
        aligning = true;
    }

    private void align() {
        if (aligning) {
        // if location of tape is negative, rotate left
        // if location of tape is positive, rotate right
        }
    }
}
