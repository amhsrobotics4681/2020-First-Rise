package frc.robot;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Victor;

public class Shooter {
    private Victor m_shooterLeft;
    private Victor m_shooterRight;
    private SpeedControllerGroup m_shooter;
    private int timer;
    
    public void shooterInit(){
        m_shooterLeft = new Victor(Constants.PWM_BallShooterL); 
        m_shooterRight = new Victor(Constants.PWM_BallShooterR);
        m_shooter = new SpeedControllerGroup(m_shooterLeft, m_shooterRight);
        timer = 0;
    }

    public void standardShooting() { // engages shooting system - IS PERIODIC
        timer++;
        if (timer > 20 && timer < 200)
            m_shooter.set(Constants.kShooterSpeed);
        else
            m_shooter.set(0);
    }
    public void fullShooting() {//Same as resetShooter but instead shoots at max capacity
        timer++;
        if (timer > 20 && timer < 200)
            m_shooter.set(1);
        else
            m_shooter.set(0);
    }
    public void killShooter() {//Shuts off Shooter regardless of current status
        timer = 200;
    }

    public void resetTimer() { //to be called when entering status "driving"/"climbing" only
        timer = 0;
    }

    public boolean getEjecting(){//Getter method for the indexer
        return (timer > 40);
    }
}
