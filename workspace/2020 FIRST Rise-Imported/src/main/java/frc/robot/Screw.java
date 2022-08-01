package frc.robot;
import edu.wpi.first.wpilibj.DigitalInput;
//import com.ctre.phoenix.motorcontrol.can.TalonSRX;
//import com.ctre.phoenix.motorcontrol.ControlMode;

public class Screw {
    //TalonSRX m_screw;
    DigitalInput m_screwStop;
    boolean screwAtElevation = true;
    int encoderTarget;

    public Screw(){
        //m_screw = new TalonSRX(Constants.CAN_Screw);
        m_screwStop = new DigitalInput(Constants.DIO_ScrewSwitch);//used to determine if screw is all the way back just in case so we dont lock it up
        encoderTarget = 0;
        //m_screw.setSelectedSensorPosition(0);
    }
    //public double getPosition() {
        //return m_screw.getSelectedSensorPosition();
    //}
    public void setSpeed(double speed) { // speed E[-1.0, 1.0]
        //m_screw.set(ControlMode.PercentOutput, speed);
    }
    public void resetScrew() {
        //m_screw.setSelectedSensorPosition(0);
    }
    public void adjustScrew() {//If too far, go back. If too close, go forward
        /*if (m_screwStop.get()) {
            m_screw.set(ControlMode.PercentOutput, 0);
            screwAtElevation = true;
        } else if (m_screw.getSelectedSensorPosition() < (encoderTarget - 3000)) {
            m_screw.set(ControlMode.PercentOutput, 1);
            screwAtElevation = false;
        } else if (m_screw.getSelectedSensorPosition() > (encoderTarget + 3000)) {
            m_screw.set(ControlMode.PercentOutput, -1);
            screwAtElevation = false;
        } else { screwAtElevation = true; }*/
        screwAtElevation = true;
    }

    public void convertElevation(int distance) {//If less than 10 or more than 30 feet, go to base position
        if (distance < 120) {
            encoderTarget = 0;
        } else if (distance > 360) {
            encoderTarget = 0;  // arbitrary, TBD
        } else {
            encoderTarget = 60000+250*distance-Math.abs(750*distance-180000);
            // arbitrary formula, very much TBD
        }
    }
}