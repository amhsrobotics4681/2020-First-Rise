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
    Victor m_shooter;
    DigitalInput m_intakeSwitch;
    int ballCount;
    int failsafe;
    private int timer;
    int maxTime;
    boolean intakeSwitchPressed;
    boolean currentlyShooting;
    boolean intakeOn;

    public void ballSystemInit() {
        m_intake = new Victor(Constants.PWM_BallIntake);
        m_indexer = new Victor(Constants.PWM_BallIndexer);
        m_shooter = new Victor(Constants.PWM_BallShooter); 
        m_intakeSwitch = new DigitalInput(Constants.DIO_BallCounter);
        ballCount = 0; //May have to change to 3
        failsafe = 1;
        timer = 300;
        maxTime = 200; // = seconds * 50
        intakeSwitchPressed = false;
        currentlyShooting = true;
        intakeOn = false;
    }

    public void mainMethod() {
        // INTAKE CODE- spin until system is full
        if(!m_intakeSwitch.get()) System.out.println(timer);
        if(ballCount < 5 && intakeOn){
            m_intake.set(Constants.kIntakeSpeed*failsafe);
        } else {
            m_intake.set(0);
        }
        // INDEXER CODE- spin when ball is collected and count balls in system
        if(m_intakeSwitch.get()){
            //m_indexer.set(Constants.kIndexSpeed); uncomment later
            if(!intakeSwitchPressed) {
                ballCount ++;
                intakeSwitchPressed = true;
            }
        } else {
            intakeSwitchPressed = false;
            if(!currentlyShooting) {
                m_indexer.set(0);
            } // "Happy now nerds?" (Ben Madow) 
        }
        // SHOOTER CODE- run shooter until timer runs out
        if(timer > maxTime) {
            m_shooter.set(0);
            currentlyShooting = false;
        }
        timer++;
    }

    public void resetShooter(){ //Resets timer and engages shooting system
        timer = 0;
        ballCount = 0;
        m_shooter.set(Constants.kShooterSpeed);
        m_indexer.set(Constants.kIndexSpeed);
        currentlyShooting = true;
    }

    public void toggleIntake(){
        intakeOn = !intakeOn;
    }
}
