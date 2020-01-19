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
//import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;
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
    private DigitalInput m_raspberryPiX;
    private DigitalInput m_raspberryPiY;
    private int xLocation;
    private int yLocation;
    private boolean aligningX = false;



    private Victor m_left;
    private Victor m_right;
    //private DifferentialDrive m_drive;
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

        m_left = new Victor(Constants.kLeftMotorInput);
        m_right = new Victor(Constants.kRightMotorInput);
        //m_drive = new DifferentialDrive(m_left, m_right);

        m_raspberryPiX = new DigitalInput(Constants.kRaspberryPiXInput);
        m_raspberryPiY = new DigitalInput(Constants.kRaspberryPiYInput);
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
        m_wheel.mainMethod();
        this.alignX();
        SmartDashboard.putString("Detected Color", m_wheel.getColor());
        SmartDashboard.putNumber("Red", m_wheel.getRed());
        SmartDashboard.putNumber("Green", m_wheel.getGreen());
        SmartDashboard.putNumber("Blue", m_wheel.getBlue());
        SmartDashboard.putNumber("Confidence", m_wheel.getConfidence());
        SmartDashboard.putNumber("Proximity", m_wheel.getProximity());
        m_right.set(controller.getRawAxis(3));
        m_left.set(controller.getRawAxis(1));
        if (controller.getRawButtonPressed(Constants.kClimberExtendButton)){
            m_climber.extending();
        }
        if (controller.getRawButtonPressed(Constants.kClimberContractButton)){
            m_climber.contracting();
        }
        if (controller.getRawButton(Constants.kClimberStopButton)){
            m_climber.stop();
        }
        if (controller.getRawButtonPressed(Constants.kRotateSetColorButton)){
            m_wheel.setColor();
        }
        if (controller.getRawButtonPressed(Constants.kRotateRevolutionsButton)){
            m_wheel.startRotating();
            System.out.println("PRessed");
        }
        if (controller.getRawButtonPressed(Constants.kManualIntakeOverrideButton)){
            if (m_ball.getIntakeStatus() == true){
                m_ball.manualIntakeOverrideOff();
            }
            else if (m_ball.getIntakeStatus() == false){
                m_ball.manualIntakeOverrideOff();
            }
        }
        if (controller.getRawButtonPressed(Constants.kManualWheelOverrideButton)){
            //Figure out how you want to do this one
        }
        if (controller.getRawButtonPressed(Constants.kManualShootingOverrideButton)){
            m_ball.toggleShooting();
        }
        if (controller.getRawButtonPressed(Constants.kAlignButton)){
            aligningX = true;
        }
    }

    @Override
    public void testPeriodic() {
    }

    public void alignX(){
        if (aligningX == true){
            //xLocation = m_raspberryPiX.get();
            xLocation = 0; //uncomment line above once figured out how to get value
            if (xLocation < 20 && xLocation > -20){
                m_left.set(0);
                m_right.set(0);
                aligningX = false;
            }
            if (xLocation < -20){
                m_left.set(Constants.kDrivingSpeed);
            }
            if (xLocation > 20){
                m_right.set(Constants.kDrivingSpeed);
            }
        }
    }
    
}
