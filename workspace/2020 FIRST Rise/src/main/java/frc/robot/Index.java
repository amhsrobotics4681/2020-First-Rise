package frc.robot;

import edu.wpi.first.wpilibj.Victor;

public class Index {
    private Victor m_index;
    boolean currentlySpitting;
    boolean indexOn;
    
    public void indexInit(){
        m_index = new Victor(Constants.PWM_BallIndexer);
        currentlySpitting = false;
        indexOn = false;
    }

    public void toggleSpit(){
        indexOn = false;//Sends balls back out the front. Works in tandem with intake
        currentlySpitting = !currentlySpitting;
    }
    public void toggleIndex() {//Toggles whether on or off
        indexOn = !indexOn;
        currentlySpitting = false;
    }
    public void killIndex(){//Shuts off Intake regardless of current status
        indexOn = false;
    }
    public void reviveIndex(){//Turns on Intake regardless of current status
        indexOn = true;
    }
    public void mainMethod(boolean intakeIndexSpinning, boolean shooterIndexSpinning){//Takes into account whether intake or shooter needs it. If not, turns off
        if (shooterIndexSpinning){
            m_index.set(Constants.kEjectionSpeed);
        }
        else if (intakeIndexSpinning){
            m_index.set(Constants.kIndexSpeed);
        }
        else if (indexOn){
            m_index.set(Constants.kIndexSpeed);
        }
        else if (currentlySpitting){
            m_index.set(Constants.kSpitSpeed);
        }
        else{
            m_index.set(0);
        }
    }

}