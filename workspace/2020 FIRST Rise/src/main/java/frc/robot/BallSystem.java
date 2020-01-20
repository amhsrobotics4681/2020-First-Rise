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
    private boolean intakeSwitchPressed = false;
    private boolean intakeOn = false;
    private int currentBallTotal = 0;
    private int timer = 0;
    private DigitalInput m_intakeSwitch;
    private Victor m_intake;
    private Victor m_indexer;
    private Victor m_shooter;

    public void ballSystemInit() {
        m_intake = new Victor(Constants.PWM_Intake);
        m_indexer = new Victor(Constants.PWM_Indexer);
        m_shooter = new Victor(Constants.PWM_Shooter); //Based on assumption that the motor controller goes to both
        m_intakeSwitch = new DigitalInput(Constants.DIO_IntakeSwitch);
    }

    public void mainMethod() {
        // intake code
        if (intakeOn && currentBallTotal < 5) {
            m_intake.set(Constants.kIntakeSpeed);
        } else {
            m_intake.set(0);
        }
        // indexer code
        if (m_intakeSwitch.get()) {
            m_indexer.set(Constants.kIndexSpeed);
            if (!intakeSwitchPressed) // i.e. first press
                currentBallTotal++;
            intakeSwitchPressed = true;
        } else {
            intakeSwitchPressed = false;
            m_indexer.set(0);
        }
        // shooter code
        // #secondsToRun / 0.02 --> 6 sec. = 300 timer
        timer++;
        if (timer > 300) {
            m_shooter.set(0);
            m_indexer.set(0);
        }
    }

    public void toggleIntake() {
        intakeOn = !intakeOn;
    }

    public void resetShooter() {
        timer = 0;
        intakeOn = false;
        currentBallTotal = 0;
        m_shooter.set(Constants.kShooterSpeed);
        m_indexer.set(Constants.kEjectionSpeed);
    }
    /*
    // I see the value in toggling the shooter in the event
    // we aren't lined up and need to stop losing our balls.
    // Also, the problem with too many overrides is not enough buttons.
    public void toggleShooting() {
        if (currentlyShooting == false){
            currentlyShooting = true;
            m_wheel.set(Constants.kEjectionSpeed);
            m_shooter.set(Constants.kShooterSpeed);
        }
        else if (currentlyShooting == true){
            currentlyShooting = false;
            m_wheel.set(0);
            m_shooter.set(0);
        }
    }*/
}
