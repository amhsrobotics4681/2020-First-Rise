package frc.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;

public class Index {
    private Victor m_index;
    private DigitalInput m_switchFront;
    private DigitalInput m_switchBack;
    private boolean currentlySpitting, currentlyEjecting;
    
    public void indexInit(){
        m_index = new Victor(Constants.PWM_BallIndexer);
        m_switchFront = new DigitalInput(Constants.DIO_BallSwitchFront);
        m_switchBack = new DigitalInput(Constants.DIO_BallSwitchBack);
        currentlySpitting = false;
        currentlyEjecting = false;
    }

    public void setSpitting(boolean state) {
        currentlySpitting = state;
    }
    public boolean getSpitting() {
        return currentlySpitting;
    }
    public void setEjecting(boolean state) {
        currentlyEjecting = state;
    }
    public boolean getEjecting() {
        return currentlyEjecting;
    }

    public void mainMethod(){
        if (currentlyEjecting) {
            m_index.set(Constants.kEjectionSpeed);
        } else if (currentlySpitting) {
            m_index.set(Constants.kSpitSpeed);
        } else if (m_switchFront.get()) {
            m_index.set(Constants.kIndexSpeed);
        } else if (m_switchBack.get()){
            m_index.set(0);
        }
        
    }

}