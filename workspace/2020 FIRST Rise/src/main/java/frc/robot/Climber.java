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
    private String status;

    public void climberInit() {
        m_pulley = new Victor(Constants.PWM_ClimberPulley); //fill in PWM port
        m_servo = new Servo(Constants.PWM_Servo);
        status="";
        m_servo.setAngle(180);
    }
    public void extending() {
        status = "Climbing";

    }
    public void contracting(){
        status = "Contracting";
    }
    public void stop() {
        status = "Stationary";
    }
    public void mainMethod(){
        System.out.println(m_servo.getAngle());
        if (status.equals("Stationary")){
            m_pulley.set(0);
            m_servo.setAngle(180);
        } else if (status.equals("Climbing")){
            m_servo.setAngle(135);
            if (m_servo.getAngle() > 140){
                m_pulley.set(-.3);//Goes down until enough weight removed to pull servo
            }
            if (m_servo.getAngle() < 140){
                m_pulley.set(.3);
            }
        } else if (status.equals("Contracting")){
            m_pulley.set(-.3);
            m_servo.setAngle(180);
        }
    }

}
