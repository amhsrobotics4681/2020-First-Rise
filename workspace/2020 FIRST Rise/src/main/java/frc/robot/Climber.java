/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Servo;

public class Climber {
    private Victor m_pulley;
    private Servo m_servo;

    public void climberInit() {
        m_pulley = new Victor(Constants.PWM_ClimberPulley); //fill in PWM port
        //m_servo = new Servo(9);
    }
    public void extending() {
        m_pulley.set(1);
        //m_servo.setAngle(20);
    } 
    public void contracting() {
        m_pulley.set(-1);
    }
    public void stop() {
        m_pulley.set(0);
        //m_servo.setAngle(0);
    }

}
