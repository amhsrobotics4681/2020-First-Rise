/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;
// Need to import dio limit switch junk i don't know what its called

public class BallSystem {
    Victor m_intake;
    Victor m_indexer;
    Victor m_shooter;
    DigitalInput m_intakeSwitch;
    int ballCount;
    int failsafe;
    int timer;
    int maxTime;
    boolean intakeSwitchPressed;
    boolean currentlyShooting;

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
        currentlyShooting = false;
    }

    public void mainMethod() {
        // INTAKE CODE
        if(ballCount < 5){
            m_intake.set(Constants.kIntakeSpeed*failsafe);
        } else {
            m_intake.set(0);
        }
        // INDEXER CODE
        if(m_intakeSwitch.get()){
            m_indexer.set(Constants.kIndexSpeed);
            if(!intakeSwitchPressed) {
                ballCount ++;
                intakeSwitchPressed = true;
            }
        } else {
            intakeSwitchPressed = false;
            if(!currentlyShooting) {m_indexer.set(0);} // eric did this, not chris (surprise surprise)
        }
        // SHOOTER CODE
        if(timer > maxTime) {
            m_shooter.set(0);
            currentlyShooting = false;
        }
        timer++;
    }

    public void resetShooter(){
        timer = 0;
        ballCount = 0;
        m_shooter.set(Constants.kShooterSpeed);
        m_indexer.set(Constants.kIndexSpeed);
        currentlyShooting = true;
    }
}
