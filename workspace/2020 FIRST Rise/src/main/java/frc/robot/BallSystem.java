/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PWMTalonSRX;

public class BallSystem {
    Victor m_intake, m_indexer, m_shooterLeft, m_shooterRight;
    PWMTalonSRX m_screw;
    DigitalInput m_intakeSwitch;
    DigitalInput m_intakeSwitch2;
    DigitalInput m_screwStop;
    private int timer;
    int maxTime;
    boolean intakeSwitchPressed;
    boolean currentlyShooting;
    boolean intakeOn;
    boolean spitting = false;
    boolean switchPressed;
    boolean currentlySpinning;
    boolean intakeDead;
    int currentBallCount;

    public void ballSystemInit() {
        m_intake = new Victor(Constants.PWM_BallIntake);
        m_intake.setInverted(true);
        m_indexer = new Victor(Constants.PWM_BallIndexer);
        m_shooterLeft = new Victor(Constants.PWM_BallShooterL); 
        m_shooterRight = new Victor(Constants.PWM_BallShooterR);
        m_intakeSwitch = new DigitalInput(Constants.DIO_BallCounter);
        m_intakeSwitch2 = new DigitalInput(Constants.DIO_BallCounter2);
        m_screw = new PWMTalonSRX(Constants.PWM_Screw);
        m_screwStop = new DigitalInput(Constants.DIO_ScrewSwitch);
        timer = 0;
        maxTime = 200; // = seconds * 50
        intakeSwitchPressed = false;
        currentlyShooting = false;
        currentlySpinning = false;
        intakeOn = false;
        intakeDead = false;
        currentBallCount = 0;
    }
    public void screwSpeed(double speed) {
        // if switch pressed and leaning back, stop screw
        if (m_screwStop.get() && (speed < 0)) {
            m_screw.set(0);
        } else {
            m_screw.set(speed);
        }
    }

    public int ballCount(){
        return currentBallCount;
    }

    public void toggleSpit() {
        spitting = !spitting;
        intakeOn = false;
    }
    public void toggleIntake() {
        intakeOn = !intakeOn;
        spitting = false;
    }

    public void mainMethod() {
        System.out.println(m_screw.get());
        if (intakeDead){
            m_intake.set(0);
        }
        else if (intakeOn){
            m_intake.set(Constants.kIntakeSpeed);
        } else if (spitting){
            m_intake.set(Constants.kSpitSpeed);
        } else {
            m_intake.set(0);
        }
        // INDEXER CODE
        if (currentlyShooting && (timer > 50)){
            m_indexer.set(Constants.kEjectionSpeed);
        }
        else if (spitting){
            m_indexer.set(-Constants.kIndexSpeed);
        }
        else if (m_intakeSwitch.get()){
            
            if (!currentlySpinning){
                currentBallCount ++;
                System.out.println("Pressed");
            }
            currentlySpinning = true;
            m_indexer.set(Constants.kIndexSpeed);
        }
        else if (currentlySpinning){
            if (m_intakeSwitch2.get()){
                m_indexer.set(0);
                currentlySpinning = false;
            }
        } else {
            m_indexer.set(0);
        }
        //Shooter code
        if(timer > maxTime) {
            m_shooterLeft.set(0);
            m_shooterRight.set(0);
            currentlyShooting = false;
        }
        timer ++;
    }

    public void resetShooter(){ //Resets timer and engages shooting system
        timer = 0;
        m_shooterLeft.set(Constants.kShooterSpeed);
        m_shooterRight.set(Constants.kShooterSpeed);
        currentBallCount = 0;
        currentlyShooting = true;
    }
    public void fullShooter(){
        timer = 0;
        m_shooterRight.set(1);
        m_shooterLeft.set(1);
        currentBallCount = 0;
        currentlyShooting = true;
    }
    public void killShooter(){
        m_shooterLeft.set(0);
        m_shooterRight.set(0);
        m_indexer.set(0);
    }
    public void killIntake(){
        intakeDead = true;
    }
    public void reviveIntake(){
        intakeDead = false;
    }
}
