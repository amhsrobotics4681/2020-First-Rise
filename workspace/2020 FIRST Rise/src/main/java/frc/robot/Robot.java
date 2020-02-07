/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
    private static final String kDefaultAuto = "Default";
    private static final String kCustomAuto = "My Auto";
    private String m_autoSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();

    private Joystick controller;
    private Climber m_climber;
    private Wheel m_wheel;
    private BallSystem m_ball;

    private Victor m_left;
    private Victor m_right;
    private DifferentialDrive m_drive;
    @Override
    public void robotInit() {
        m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
        m_chooser.addOption("My Auto", kCustomAuto);
        SmartDashboard.putData("Auto choices", m_chooser);

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
    }

    @Override
    public void autonomousInit() {
        m_autoSelected = m_chooser.getSelected();
        m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
        System.out.println("Auto selected: " + m_autoSelected);
    }

    @Override
    public void autonomousPeriodic() {
        switch (m_autoSelected) {
        case kCustomAuto:
            // Put custom auto code here
            break;
        case kDefaultAuto:
        default:
            // Put default auto code here
            break;
        }
    } 
    
    @Override
    public void teleopPeriodic() {
        m_ball.mainMethod();
        SmartDashboard.putString("Detected Color", m_wheel.getColor());
        SmartDashboard.putNumber("Red", m_wheel.getRed());
        SmartDashboard.putNumber("Green", m_wheel.getGreen());
        SmartDashboard.putNumber("Blue", m_wheel.getBlue());
        SmartDashboard.putNumber("Confidence", m_wheel.getConfidence());
        SmartDashboard.putNumber("Proximity", m_wheel.getProximity());
        m_drive.arcadeDrive(controller.getRawAxis(3), controller.getRawAxis(2));
        if (controller.getRawButtonPressed(Constants.bExtendClimber)){
            m_climber.extending();
        }
        if (controller.getRawButtonPressed(Constants.bContractClimber)){
            m_climber.contracting();
        }
        if (controller.getRawButtonPressed(Constants.bStopClimber)){
            m_climber.stop();
        }
        if (controller.getRawButtonPressed(Constants.bPositionControl)){
            
        }
        if (controller.getRawButtonPressed(Constants.bRotationControl)){
            
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

    @Override
    public void testPeriodic() {
    }
}
