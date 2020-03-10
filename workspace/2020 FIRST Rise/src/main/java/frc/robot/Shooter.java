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
    }
    public boolean getIndexSpinning(){
        return indexCurrentlyOn;
    }
    public void mainMethod(){
        if (currentlyShooting){
            if (timer > 50){
                indexCurrentlyOn = true;
            }
            else{
                indexCurrentlyOn = false;
            }
        }
        if (timer > maxTime){
            m_shooterRight.set(0);
            m_shooterLeft.set(0);
            currentlyShooting = false;
        }
    }
}
