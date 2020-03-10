package frc.robot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;

public class Shooter {
    private Victor m_shooterLeft;
    private Victor m_shooterRight;
    private int timer;
    private boolean currentlyShooting;
    private boolean indexCurrentlyOn;
    private final int maxTime = 200;
    
    
    
    public void shooterInit(){
        m_shooterLeft = new Victor(Constants.PWM_BallShooterL); 
        m_shooterRight = new Victor(Constants.PWM_BallShooterR);
        timer = 0;
        currentlyShooting = false;
        indexCurrentlyOn = false;
    }
    public void resetShooter(){ //Resets timer and engages shooting system
        timer = 0;
        m_shooterLeft.set(Constants.kShooterSpeed);
        m_shooterRight.set(Constants.kShooterSpeed);
        currentlyShooting = true;
        //intakeOn = false; spitting = false;
    }
    public void fullShooter() {//Same as resetShooter but instead shoots at max capacity
        timer = 0;
        m_shooterRight.set(1);
        m_shooterLeft.set(1);
        currentlyShooting = true;
    }
    public void killShooter(){//Shuts off Shooter regardless of current status
        timer = maxTime;
        m_shooterRight.set(0);
        m_shooterLeft.set(0);
    }
    public boolean getIndexSpinning(){//Getter method for the indexer
        return indexCurrentlyOn;
    }
    public void mainMethod(){//waits one second before turning the indexer on so shooter can come to speed
        if (currentlyShooting){
            if (timer > 50){//timer increases every .02 seconds, therefore 50*.02 == 1 seconds
                indexCurrentlyOn = true;
            }
            else{//if shooter still speeding up, keep indexer off
                indexCurrentlyOn = false;
            }
        }
        if (timer > maxTime){//after shooting for 4 seconds, turn the motors off
            m_shooterRight.set(0);
            m_shooterLeft.set(0);
            currentlyShooting = false;
        }
    }
}
