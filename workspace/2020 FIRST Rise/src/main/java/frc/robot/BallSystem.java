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
    Victor m_intake, m_indexer, m_shooterLeft, m_shooterRight, m_screw;
    DigitalInput m_intakeSwitch;
    private int timer;
    private int switchEnd;
    int maxTime;
    boolean intakeSwitchPressed;
    boolean currentlyShooting;
    boolean intakeOn;
    boolean indexerOn;
    boolean spitting = false;
    boolean switchPressed;

    public void ballSystemInit() {
        switchEnd = -1;
        m_intake = new Victor(Constants.PWM_BallIntake);
        m_indexer = new Victor(Constants.PWM_BallIndexer);
        m_shooterLeft = new Victor(Constants.PWM_BallShooterL); 
        m_shooterRight = new Victor(Constants.PWM_BallShooterR);
        m_intakeSwitch = new DigitalInput(Constants.DIO_BallCounter);
        m_screw = new Victor(Constants.PWM_Screw);
        timer = 300;
        maxTime = 200; // = seconds * 50
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
        System.out.println(!m_intakeSwitch.get()); //invert
        // INTAKE CODE- spin until system is full
        if (intakeOn) {
            m_intake.set(Constants.kIntakeSpeed);
        } else {
            if (!spitting)
                m_intake.set(0);
        }

        // INDEXER CODE- spin when ball is collected and count balls in system
        if (indexerOn) {
            m_indexer.set(Constants.kIndexSpeed);
        } else {
            if (!currentlyShooting) {
                m_indexer.set(0);
            }
        }        
        //For a switch
        if(!m_intakeSwitch.get()) {
            m_indexer.set(Constants.kIndexSpeed);
            if (!switchPressed){
                switchEnd = timer + 30;
                switchPressed = true;
            }
        } else {
            if(!currentlyShooting && switchEnd < timer) //delete '&& !indexerOn' once index toggle system removed
                m_indexer.set(0);
                switchPressed = false;
        }
        // SHOOTER CODE- run shooter until timer runs out
        if(timer > maxTime) {
            m_shooterLeft.set(0);
            m_shooterRight.set(0);
            currentlyShooting = false;
        }
        timer++;
    }

    public void spit() { //it's a toggle
        spitting = !spitting;
        if (spitting)
            m_intake.set(.5);
        else
            m_intake.set(0);
        }

    public void resetShooter(){ //Resets timer and engages shooting system
        timer = 0;
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
        if (!spitting)
            intakeOn = !intakeOn;
    }
}
