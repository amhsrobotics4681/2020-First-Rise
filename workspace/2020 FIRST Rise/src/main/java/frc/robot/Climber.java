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
    private boolean climbing;
    private int counter;
    private String status;
    public void climberInit() {
        m_pulley = new Victor(Constants.PWM_ClimberPulley); //fill in PWM port
        m_servo = new Servo(Constants.PWM_Servo);
    
    }
    public void extending() {
        status = "Climbing";

    } 
    public void contracting() {
        status = "Contracting";
    }
    public void stop() {
        status = "Stationary";
    }
    public void MainMethod(){
        if (status.equals("Stationary")){
            m_servo.set(0);
        }
        else if (status.equals("Climbing")){
            m_servo.setAngle(90);
            if (m_servo.getAngle() < 45){
                m_servo.set(-1);//Goes down until enough weight removed to pull servo
            }
            if (m_servo.getAngle() > 45){
                m_servo.set(1);
            }
        }
        else if (status.equals("Contracting")){
            m_servo.set(-1);
        }
    }

}
