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
    private boolean switchPressed = false;
    private int currentBallTotal = 0;
    private DigitalInput m_DIOlimitSwitchWheel;
    private Victor m_wheel;
    private final double kWheelSpeed = .3; //arbitrary value

    public void ballSystemInit() {
        m_intake = new Victor(2);
        m_hopper = new Victor(3);
        m_shooter = new Victor(4); 
        m_wheel = new Victor(5);
        m_DIOlimitSwitchWheel = new DigitalInput(3);
    }
    public void mainMethod() {
        if (m_DIOlimitSwitchWheel.get() && !switchPressed){
            switchPressed = true;
            m_wheel.set(kWheelSpeed);
            currentBallTotal ++;	
        }
        if (switchPressed){
            if (!m_DIOlimitSwitchWheel.get()){
                m_wheel.set(0);
                switchPressed = false;
            }
        }
    
    }
    public boolean returnWheelSwtich(){
        return m_DIOlimitSwitchWheel.get();
    }
    public void manualWheelOverrideOn(){
        m_wheel.set(kWheelSpeed);
    }
    public void manualWheelOverrideOff(){
        m_wheel.set(0);
    }
}
