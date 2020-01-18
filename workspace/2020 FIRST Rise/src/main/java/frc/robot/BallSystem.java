/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;
// Need to import dio limit switch junk i don't know what its called

public class BallSystem {
    Victor m_intake;
    Victor m_hopper;
    Victor m_shooter;
    private boolean wheelSwitchPressed = false;
    private boolean shooterSwitchPressed = false;
    private int currentBallTotal = 0;
    private DigitalInput m_DIOlimitSwitchWheel;
    private DigitalInput m_DIOlimitSwitchShooter;
    private Victor m_wheel;
    private boolean intakeOn = true;
    private boolean currentlyShooting = false;

    public void ballSystemInit() {
        m_intake = new Victor(2);
        m_hopper = new Victor(3);
        m_shooter = new Victor(4); 
        m_wheel = new Victor(5);
        m_DIOlimitSwitchWheel = new DigitalInput(Constants.kDIOLimitSwitchWheelInput);
        m_DIOlimitSwitchWheel = new DigitalInput(Constants.kDIOLimitSwitchWheelShooter);
    }
    public void mainMethod() {
        if (currentBallTotal > 4){
            intakeOn = false;
        }
        else{
            intakeOn = true;
        }
        if (intakeOn){
            m_intake.set(Constants.kIntakeSpeed);
        }
        if (currentlyShooting){
            if (currentBallTotal == 0){
                currentlyShooting = false;
                m_wheel.set(0);
            }
            //Add in code for shooting motors here
        }
        if (m_DIOlimitSwitchWheel.get() && !wheelSwitchPressed){
            wheelSwitchPressed = true;
            m_wheel.set(Constants.kWheelSpeed);
            currentBallTotal ++;	
        }
        if (wheelSwitchPressed){
            if (!m_DIOlimitSwitchWheel.get()){
                m_wheel.set(0);
                wheelSwitchPressed = false;
            }
        }
        if (m_DIOlimitSwitchShooter.get() && !shooterSwitchPressed){
            wheelSwitchPressed = true;
            currentBallTotal --;	
        }
        if (wheelSwitchPressed){
            if (!m_DIOlimitSwitchWheel.get()){
                wheelSwitchPressed = false;
            }
        }
    
    }
    public boolean returnWheelSwtich(){
        return m_DIOlimitSwitchWheel.get();
    }
    public void beginShooting(){
        currentlyShooting = true;
        m_wheel.set(Constants.kWheelShootingSpeed);
    }
    public void manualWheelOverrideOn(){
        m_wheel.set(Constants.kWheelSpeed);
    }
    public void manualWheelOverrideOff(){
        m_wheel.set(0);
    }
    public void manualIntakeOverrideOn(){
        m_intake.set(Constants.kIntakeSpeed);
    }
    public void manualIntakeOverrideOff(){
        m_intake.set(0);
    }
}
