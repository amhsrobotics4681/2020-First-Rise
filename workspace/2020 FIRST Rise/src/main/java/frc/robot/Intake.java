package frc.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;

public class Intake {
    private Victor m_intake;
    private DigitalInput m_intakeSwitchFront;
    private DigitalInput m_intakeSwitchBack;
    private boolean intakeOn;
    private boolean spitting;
    private boolean indexCurrentlySpinning;
    private int currentBallCount;
    
    
    public void intakeInit(){
        m_intake = new Victor(Constants.PWM_BallIntake);
        m_intake.setInverted(true);
        m_intakeSwitchFront = new DigitalInput(Constants.DIO_BallCounter);
        m_intakeSwitchBack = new DigitalInput(Constants.DIO_BallCounter2);
        intakeOn = false;

    }
    public void toggleSpit(){
        intakeOn = false;
        spitting = !spitting;
    }
    public void toggleIntake() {
        intakeOn = !intakeOn;
        spitting = false;
    }
    public void killIntake(){//Shuts off Intake regardless of current status
        intakeOn = false;
    }
    public void reviveIntake(){//Turns on Intake regardless of current status
        intakeOn = true;
    }
    public int getBallCount(){
        return currentBallCount;
    }
    public void intakeMainMethod(){
        if (intakeOn){
            m_intake.set(Constants.kIntakeSpeed);
        }
        else if (spitting){
            m_intake.set(Constants.kSpitSpeed);
        }
        if (m_intakeSwitchFront.get()){          
            if (!indexCurrentlySpinning){
                currentBallCount ++;
            }
            indexCurrentlySpinning = true;
        }
        else if (indexCurrentlySpinning){
            if (m_intakeSwitchBack.get()){
                indexCurrentlySpinning = false;
            }
        }
    }
    public boolean getIndexSpinning(){
        return indexCurrentlySpinning;
    }
}


