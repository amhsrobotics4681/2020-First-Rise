/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Victor;
//Ball intake
public class BallSystem {
    Victor m_intake;
    Victor m_hopper;
    Victor m_shooter;

    public void ballSystemInit() {
        m_intake = new Victor(2);
        m_hopper = new Victor(3);
        m_shooter = new Victor(4);
    }
}
