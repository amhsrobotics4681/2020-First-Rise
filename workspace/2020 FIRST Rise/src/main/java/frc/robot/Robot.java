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
    /**  Victor PWM definitions:
     * 0: left wheel motor
     * 1: right wheel motor
     * 2: ball intake motor
     * 3: ball hopper motor
     * 4: ball shooter motor
     * 5: control panel motor
     * 6: pulley climber motor
     * 7: winch climber motor
     */

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
        m_climber = new Climber(); //basically wash, rinse, repeat
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
        // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
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
        SmartDashboard.putString("Detected Color", m_wheel.getColor());
        SmartDashboard.putNumber("Red", m_wheel.getRed());
        SmartDashboard.putNumber("Green", m_wheel.getGreen());
        SmartDashboard.putNumber("Blue", m_wheel.getBlue());
        SmartDashboard.putNumber("Confidence", m_wheel.getConfidence());
        SmartDashboard.putNumber("Proximity", m_wheel.getProximity());
        if (controller.getRawButtonPressed(6)) {
            m_wheel.rotate(4);
        }
    }

    @Override
    public void testPeriodic() {
    }
}
