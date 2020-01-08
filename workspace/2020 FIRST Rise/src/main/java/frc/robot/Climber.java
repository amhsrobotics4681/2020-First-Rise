/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.Victor;

public class Climber {
    private Victor m_lifter;

    public void climberInit() {
        m_lifter = new Victor(0); //fill in PWM port
    }
    public void set(double speed) {
        m_lifter.set(speed);
    }
}
