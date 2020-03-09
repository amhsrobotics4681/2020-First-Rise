/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class BallSystem {
    Victor m_intake, m_indexer, m_shooterLeft, m_shooterRight;
    TalonSRX m_screw;
    DigitalInput m_intakeSwitch;
    DigitalInput m_intakeSwitch2;
    DigitalInput m_screwStop;
    private int timer;
    private final int maxTime = 200;
    boolean intakeSwitchPressed;
    boolean currentlyShooting;
    boolean intakeOn;
    boolean spitting = false;
    boolean switchPressed;
    boolean currentlySpinning;
    boolean screwAtElevation = true;
    int currentBallCount;
    int encoderTarget;

    public void ballSystemInit() {
        m_intake = new Victor(Constants.PWM_BallIntake);
        m_intake.setInverted(true);
        m_indexer = new Victor(Constants.PWM_BallIndexer);
        m_shooterLeft = new Victor(Constants.PWM_BallShooterL); 
        m_shooterRight = new Victor(Constants.PWM_BallShooterR);
        m_screw = new TalonSRX(Constants.CAN_Screw);
        m_intakeSwitch = new DigitalInput(Constants.DIO_BallCounter);
        m_intakeSwitch2 = new DigitalInput(Constants.DIO_BallCounter2);
        m_screwStop = new DigitalInput(Constants.DIO_ScrewSwitch);
        timer = 0;
        intakeSwitchPressed = false;
        currentlyShooting = false;
        currentlySpinning = false;
        intakeOn = false;
        currentBallCount = 0;
        encoderTarget = 0;
        m_screw.setSelectedSensorPosition(0);
    }

    public void adjustScrew() {//If too far, go back. If too close, go forward
        if (m_screwStop.get()) {
            m_screw.set(ControlMode.PercentOutput, 0);
            screwAtElevation = true;
        } else if (m_screw.getSelectedSensorPosition() < (encoderTarget - 1500)) {
            m_screw.set(ControlMode.PercentOutput, 1);
            screwAtElevation = false;
        } else if (m_screw.getSelectedSensorPosition() > (encoderTarget + 1500)) {
            m_screw.set(ControlMode.PercentOutput, -1);
            screwAtElevation = false;
        } else { screwAtElevation = true; }
    }

    public void convertElevation(int distance) {//If less than 10 or more than 30 feet, go to base position
        if (distance < 120) {
            encoderTarget = 0;
        } else if (distance > 360) {
            encoderTarget = 0;  // arbitrary, TBD
        } else {
            encoderTarget = 60000+250*distance-Math.abs(750*distance-180000);
            // arbitrary formula, very much TBD
        }
    }

    public int ballCount(){
        return currentBallCount;
    }

    public void toggleSpit() {//Toggles spitting (which is reversing intake and indexer)
        spitting = !spitting;
        intakeOn = false;
    }
    public void toggleIntake() {
        intakeOn = !intakeOn;
        spitting = false;
    }

    public void mainMethod() {
        //System.out.println(m_screw.getSelectedSensorPosition());
        if (intakeOn){
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
        intakeOn = false; spitting = false;
    }

    public void fullShooter() {//Same as resetShooter but instead shoots at max capacity
        timer = 0;
        m_shooterRight.set(1);
        m_shooterLeft.set(1);
        currentBallCount = 0;
        currentlyShooting = true;
    }
    public void killShooter(){//Shuts off Shooter regardless of current status
        timer = maxTime;
    }
    public void killIntake(){//Shuts off Intake regardless of current status
        intakeOn = false;
    }
    public void reviveIntake(){//Turns on Intake regardless of current status
        intakeOn = true;
    }
}
