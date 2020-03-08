/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Servo;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Climber {
    private Victor m_pulley;
    private Servo m_servo;
    private String status;
    private boolean rotatingServo;
    private int counter;
    private TalonSRX m_climber;
    private int targetHeight;
    public int mode;
    public final double maxClimberSpeed = .7;
    public final double climberSpeedMultiplier = .00003;
    public double currentClimberSpeed;
    public int difference;
    public boolean finishedClimbing;

    public void climberInit() {
        m_pulley = new Victor(Constants.PWM_ClimberPulley); //fill in PWM port
        m_servo = new Servo(Constants.PWM_Servo);
        m_climber = new TalonSRX(Constants.PWM_Climber);
        status="";
        m_servo.setAngle(180);
        rotatingServo = false;
        counter = 0;
        finishedClimbing = false;
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


    //EVERYTHING BELOW THIS POINT IS ASSUMING WE USE ENCODER ON CLIMBER
    //IF WE GET LAZY OR RUN OUT OF TIME AND DO NOT PUT AN ENCODER ON
    //COMMENT OUT OR DELETE THE CODE BELOW
    public void LowClimb(){
        status = "Rising";
        targetHeight = Constants.LowClimberHeight;
        counter = 0;
        rising();
    }
    public void RegularClimb(){
        status = "rising";
        targetHeight = Constants.ClimberHeight;
        counter = 0;
        rising();
    }
    public void HighClimb(){
        status = "rising";
        targetHeight = Constants.HighClimberHeight;
        counter = 0;
        rising();
    }
    /*Benefit of rising method:
        Person only needs to push one button and it takes you to proper height
        With encoder, we dont need to worry about falling
        Proportion control will keep at right height until retracting
        */
    public void rising(){
        m_servo.setAngle(135);
        if (counter<25){
            m_climber.set(ControlMode.PercentOutput, -.5*Constants.kPulleySpeed);
        }
        else{
            difference = (targetHeight - m_climber.getSelectedSensorPosition());
            if (difference < 4000){
                currentClimberSpeed = 0;
            }
            else {
                currentClimberSpeed = climberSpeedMultiplier*(difference);
                if (currentClimberSpeed > maxClimberSpeed) {
                    currentClimberSpeed = maxClimberSpeed;
                }
            }
            m_climber.set(ControlMode.PercentOutput, currentClimberSpeed);
        }
    }

}
