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
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;

public class Robot extends TimedRobot {
    private static final String kDefaultAuto = "Default";
    private static final String kCustomAuto = "My Auto";
    private String m_autoSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();
    private String gameData;

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

        m_raspberryPiX = new DigitalInput(Constants.kRaspberryPiXInput);
        m_raspberryPiY = new DigitalInput(Constants.kRaspberryPiYInput);
        m_left = new Victor(Constants.PWM_LeftTreads);
        m_right = new Victor(Constants.PWM_RightTreads);
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
        m_ball.mainMethod();
        m_right.set(controller.getRawAxis(3));
        m_left.set(controller.getRawAxis(1));
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        
        if (controller.getRawButtonPressed(Constants.bExtendClimber))
            m_climber.extending();
        if (controller.getRawButtonPressed(Constants.bContractClimber))
            m_climber.contracting();
        if (controller.getRawButton(Constants.bStopClimber))
            m_climber.stop();
        
        if (controller.getRawButtonPressed(Constants.bPositionControl))
            m_wheel.positionControl(gameData);
        if (controller.getRawButtonPressed(Constants.bRotationControl))
            m_wheel.rotationControl();
        
        if (controller.getRawButtonPressed(Constants.bIntakeToggle))
            m_ball.toggleIntake();
        if (controller.getRawButtonPressed(Constants.bResetShooter))
            m_ball.resetShooter(); // we need to decide on 'all in' or toggle

        if (controller.getRawButtonPressed(Constants.bAlignRobot))
            aligningX = true;
        /*
        SmartDashboard.putString("Detected Color", m_wheel.getColor());
        SmartDashboard.putNumber("Red", m_wheel.getRed());
        SmartDashboard.putNumber("Green", m_wheel.getGreen());
        SmartDashboard.putNumber("Blue", m_wheel.getBlue());
        SmartDashboard.putNumber("Confidence", m_wheel.getConfidence());
        SmartDashboard.putNumber("Proximity", m_wheel.getProximity());
        */
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
                m_left.set(Constants.kAligningSpeed);
            }
            if (xLocation > 20){
                m_right.set(Constants.kAligningSpeed);
            }
        }
    }
    
}
