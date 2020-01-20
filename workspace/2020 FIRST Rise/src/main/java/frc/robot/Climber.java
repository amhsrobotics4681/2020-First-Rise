/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.Victor;

public class Climber {
    private Victor m_pulley;
    private Victor m_winch;

    public void climberInit() {
        m_pulley = new Victor(Constants.PWM_Pulley);
        m_winch = new Victor(Constants.PWM_Winch);
    }
    public void extending() {
        m_pulley.set(Constants.kPulleySpeed);
    }
    public void contracting() {
        m_winch.set(Constants.kWinchSpeed);
    }
    public void stop() {
        m_pulley.set(0);
        m_winch.set(0);
    }

}
