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
    private boolean rotatingServo;
    private int counter;

    public void climberInit() {
        m_pulley = new Victor(Constants.PWM_ClimberPulley); //fill in PWM port
        m_servo = new Servo(Constants.PWM_Servo);
        status="";
        m_servo.setAngle(180);
        rotatingServo = false;
        counter = 0;
    }
    public void extending() {
        status = "Climbing";
        if (!rotatingServo){
            rotatingServo = true;
            counter = 0;
        }

    }
    public void contracting(){
        status = "Contracting";
        rotatingServo = false;
    }
    public void stop() {
        status = "Stationary";
        rotatingServo = false;
    }
    public void mainMethod(){
        /* UNDER NO CIRCUMSTANCE SHOULD THIS BE EDITED
        I MEAN IT, DONT EVEN THINK ABOUT IT
        YOU WILL BREAK THE ROBOT
        I AM WATCHING YOU
        -SP*/
        System.out.println(m_servo.getAngle());
        if (status.equals("Stationary")){
            m_pulley.set(0);
            m_servo.setAngle(180);
        } else if (status.equals("Climbing")){
            m_servo.setAngle(135);
            if (counter < 25){
                m_pulley.set(-.5*(Constants.kPulleySpeed));//Goes down until enough weight removed to pull servo
            }
            else{
                m_pulley.set(Constants.kPulleySpeed);
            }
        } else if (status.equals("Contracting")){
            m_pulley.set(-2*Constants.kPulleySpeed);
            m_servo.setAngle(180);
        }
    counter += 1;
    }

}
