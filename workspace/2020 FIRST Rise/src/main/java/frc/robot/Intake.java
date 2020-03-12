package frc.robot;

import edu.wpi.first.wpilibj.Victor;

public class Intake {
    private Victor m_intake;
    private boolean intakeOn;
    private boolean spitting;   
    
    public void intakeInit(){
        m_intake = new Victor(Constants.PWM_BallIntake);
        m_intake.setInverted(true);
        intakeOn = false;
        spitting = false;
    }
    
    public void setSpitting(boolean state){//Sends balls out the front. WOrks in tandem with index
        if (state) m_intake.set(Constants.kSpitSpeed);
        else m_intake.set(0);
        spitting = state;
        intakeOn = false;
    }
    public void setIntake(boolean state) {//Duh, anyone should be able to get this one
        if (state) m_intake.set(Constants.kIntakeSpeed);
        else m_intake.set(0);
        intakeOn = state;
        spitting = false;
    }
    public boolean getSpitting() {
        return spitting;
    }
    public boolean getIntake() {
        return intakeOn;
    }
}


