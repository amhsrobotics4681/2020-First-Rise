/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Servo;
//import com.ctre.phoenix.motorcontrol.ControlMode;
//import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Climber {
    private Victor m_pulley;
    private Servo m_servo;
    private String status;
    private boolean rotatingServo;
    private int counter;
    //Everything below here is for encoder climber
    //private TalonSRX m_climber;
    private double targetHeight;
    public int mode;
    public final double maxClimberSpeed = .7;
    public final double climberSpeedMultiplier = .00003;//arbitrary, find actual value
    public final int climberPullingIncrement = 40000;//arbitrary, find actual value
    public final int differenceMargin = 4000; //arbitrary, find actual value
    public double currentClimberSpeed;
    public double difference;
    public boolean finishedClimbing;

    public Climber() {
        m_pulley = new Victor(Constants.PWM_ClimberPulley); //fill in PWM port
        m_servo = new Servo(Constants.PWM_Servo);
        //m_climber = new TalonSRX(Constants.CAN_Climber);
        status="";
        m_servo.setAngle(180);
        rotatingServo = false;
        counter = 0;
        finishedClimbing = false;
    }
    public void extending() {
        status = "Extending";
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
        } else if (status.equals("Extending")){
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
    }
    public void RegularClimb(){
        status = "Rising";
        targetHeight = Constants.ClimberHeight;
        counter = 0;
    }
    public void HighClimb(){
        status = "Rising";
        targetHeight = Constants.HighClimberHeight;
        counter = 0;
    }
    public void startPulling(){
        status = "Pulling";
        //targetHeight = m_climber.getSelectedSensorPosition() - climberPullingIncrement;
    }
        /** Benefit of rising method:
         * Person only needs to push one button and it takes you to proper height
         * With encoder, we dont need to worry about falling
         * Proportion control will keep at right height until retracting
         */
        /** Your Honor, may I respond? (Yes)
         * If we use an encoder, limit it to detecting the max height for the climber
         * Less complex code, and we don't have to worry too much about buttons and math
         */
    public void rising(){
        m_servo.setAngle(135);
        if (counter<25){
            //m_climber.set(ControlMode.PercentOutput, -.5*Constants.kPulleySpeed);
        }
        else{
            //difference = (targetHeight - m_climber.getSelectedSensorPosition());
            if (difference < differenceMargin){
                currentClimberSpeed = 0;
                status = "Stopped";
            }
            else if (difference < -differenceMargin){
                currentClimberSpeed = climberSpeedMultiplier*(difference);
                if (Math.abs(currentClimberSpeed) > maxClimberSpeed) {
                    currentClimberSpeed = -maxClimberSpeed;
                }
            }
            else {
                currentClimberSpeed = climberSpeedMultiplier*(difference);
                if (currentClimberSpeed > maxClimberSpeed) {
                    currentClimberSpeed = maxClimberSpeed;
                }
            }
            //m_climber.set(ControlMode.PercentOutput, currentClimberSpeed);
        }
    }
    public void pulling(){
        //tricky thing with this one is you never know how much you are going to need to pull
        //Maybe make this pull you up 40,000 rotations every time pressed
        //That way, if you are on the bar and the bar lowers to your side, you can pull and rise another 40,000
        m_servo.setAngle(135);
        //difference = m_climber.getSelectedSensorPosition() - targetHeight;
        if (difference < differenceMargin){
            currentClimberSpeed = 0;
            status = "Stopped";
        }
        else if (difference < -differenceMargin){
            currentClimberSpeed = climberSpeedMultiplier*(difference);
                if (currentClimberSpeed > maxClimberSpeed) {
                    currentClimberSpeed = maxClimberSpeed;
                }
        }
        else {
            currentClimberSpeed = climberSpeedMultiplier*(-difference);
            if (Math.abs(currentClimberSpeed) > maxClimberSpeed) {
                currentClimberSpeed = -maxClimberSpeed;
            }
        }
        //m_climber.set(ControlMode.PercentOutput, currentClimberSpeed);
    }
    public void encoderMainMethod(){//Disengages ratchet at any moving point for safety of oscilation
        if (status.equals("Rising")){
            rising();
        }
        else if (status.equals("Pulling")){
            pulling();
        }
        else if (status.equals("Stopped")){
            m_servo.setAngle(180);
        }
    }
}
