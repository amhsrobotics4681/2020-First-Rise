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

public class Robot extends TimedRobot {
    private Joystick controller;
    private Climber m_climber;
    private Wheel m_wheel;
    private BallSystem m_ball;

    private Victor m_left;
    private Victor m_right;
    private DifferentialDrive m_drive;

    private boolean aligned;

    @Override
    public void robotInit() {
        controller = new Joystick(0);
        m_climber = new Climber();
        m_climber.climberInit();
        m_wheel = new Wheel();
        m_wheel.wheelInit();
        m_ball = new BallSystem();
        m_ball.ballSystemInit();

        m_left = new Victor(0);
        m_right = new Victor(1);
        m_drive = new DifferentialDrive(m_left, m_right);

        aligned = false;
    }

    @Override
    public void autonomousInit() {}

    @Override
    public void autonomousPeriodic() {
        align();
    } 
    
    @Override
    public void teleopPeriodic() {
        m_ball.mainMethod();
        System.out.println(controller.getPOV());
        SmartDashboard.putString("Detected Color", m_wheel.getColor());
        SmartDashboard.putNumber("Red", m_wheel.getRed());
        SmartDashboard.putNumber("Green", m_wheel.getGreen());
        SmartDashboard.putNumber("Blue", m_wheel.getBlue());
        SmartDashboard.putNumber("Confidence", m_wheel.getConfidence());
        SmartDashboard.putNumber("Proximity", m_wheel.getProximity());
        m_drive.arcadeDrive(controller.getRawAxis(3), controller.getRawAxis(2));
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
            
        }
    }

    private void align() {
        // align code!
        if (true) // condition for aligned
            aligned = true;
    }
}
