package frc.robot;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Victor;

public class Shooter {
    private Victor m_shooterLeft;
    private Victor m_shooterRight;
    private SpeedControllerGroup m_shooter;
    private int timer;
    
    public Shooter() {
        m_shooterLeft = new Victor(Constants.PWM_BallShooterL); 
        m_shooterRight = new Victor(Constants.PWM_BallShooterR);
        m_shooter = new SpeedControllerGroup(m_shooterLeft, m_shooterRight);
        timer = 0;
    }
    /**
     * Period function that sets the shooter motor's speed
     * @param full Whether or not to do full power shooting
     */
    public void startShooter(boolean full) { // engages shooting system - IS PERIODIC
        timer++;
        if (timer > 20 && timer < 200)
            m_shooter.set(full ? 1 : Constants.kShooterSpeed);
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
        return (timer > 40 && timer < 200);
    }
}
