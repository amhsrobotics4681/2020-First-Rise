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
        spitting = false;
        currentBallCount = 0;
        indexCurrentlySpinning = false;

    }
    public void toggleSpit(){//Sends balls out the front. WOrks in tandem with index
        intakeOn = false;
        spitting = !spitting;
    }
    public void toggleIntake() {//Duh, anyone should be able to get this one
        intakeOn = !intakeOn;
        spitting = false;
    }
    public void killIntake(){//Shuts off Intake regardless of current status
        intakeOn = false;
    }
    public void reviveIntake(){//Turns on Intake regardless of current status
        intakeOn = true;
    }
    public int getBallCount(){//returns current ball total as integer
        return currentBallCount;
    }
    public void resetBallCount(){//Once again, rather obvious
        currentBallCount = 0;
    }
    public void mainMethod(){//Line of priority: Intakeon, Spitting, switches
        if (intakeOn){
            m_intake.set(Constants.kIntakeSpeed);
        }
        else if (spitting){
            m_intake.set(Constants.kSpitSpeed);
        }
        else {
            m_intake.set(0);
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
    public boolean getIndexSpinning(){//Returns whether or not intake needs it for the indexer
        return indexCurrentlySpinning;
    }
}


