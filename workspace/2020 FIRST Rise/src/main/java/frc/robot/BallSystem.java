/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;

public class BallSystem {
    Victor m_intake;
    Victor m_indexer;
    Victor m_shooterLeft;
    Victor m_shooterRight;
    Victor m_screw;
    DigitalInput m_intakeSwitch;
    int ballCount;
    int failsafe;
    private int timer;
    int maxTime;
    boolean intakeSwitchPressed;
    boolean currentlyShooting;
    boolean intakeOn;
    boolean indexerOn;

    public void ballSystemInit() {
        m_intake = new Victor(Constants.PWM_BallIntake);
        m_indexer = new Victor(Constants.PWM_BallIndexer);
        m_shooterLeft = new Victor(Constants.PWM_BallShooterL); 
        m_shooterRight = new Victor(Constants.PWM_BallShooterR);
        m_intakeSwitch = new DigitalInput(Constants.DIO_BallCounter);
        m_screw = new Victor(Constants.PWM_Screw);
        ballCount = 0; //May have to change to 3
        failsafe = 1;
        timer = 300;
        maxTime = 400; // = seconds * 50
        intakeSwitchPressed = false;
        currentlyShooting = true;
        intakeOn = false;
    }
    public void screwSpeed(double speed){
        m_screw.set(speed);
    }
    public void toggleIndexer(){
        indexerOn = !indexerOn;
    }

    public void mainMethod() {
        // INTAKE CODE- spin until system is full
        System.out.println(m_intakeSwitch.get());
        if(!m_intakeSwitch.get()){
            System.out.println(timer);
        }
        System.out.println("Ball Count: " + ballCount);
        if(ballCount < 5 && intakeOn){
            m_intake.set(Constants.kIntakeSpeed*failsafe);
        } else {
            if(!currentlyShooting){
                m_intake.set(0);
            }
        }
        if (indexerOn){
            m_indexer.set(Constants.kIndexSpeed);
        }
        else{
            if (!currentlyShooting){
                m_indexer.set(0);
            }
        }
        // INDEXER CODE- spin when ball is collected and count balls in system
        
        
        
        //For a switch
        if(!m_intakeSwitch.get()){
            m_indexer.set(Constants.kIndexSpeed);
            if(!intakeSwitchPressed) {
                ballCount ++;
                intakeSwitchPressed = true;
            }
        } else {
            intakeSwitchPressed = false;
            if(!currentlyShooting && !indexerOn) { //delete '&& !indexerOn' once index toggle system removed
                m_indexer.set(0);
            }
        }
        // SHOOTER CODE- run shooter until timer runs out
        if(timer > maxTime) {
            m_shooterLeft.set(0);
            m_shooterRight.set(0);
            currentlyShooting = false;
        }
        timer++;
    }

    public void resetShooter(){ //Resets timer and engages shooting system
        timer = 0;
        ballCount = 0;
        m_shooterLeft.set(Constants.kShooterSpeed);
        m_shooterRight.set(Constants.kShooterSpeed);
        m_indexer.set(Constants.kEjectionSpeed);
        currentlyShooting = true;
    }
    public void killShooter(){
        m_shooterLeft.set(0);
        m_shooterRight.set(0);
    }
    public void toggleIntake(){
        intakeOn = !intakeOn;
    }
}
