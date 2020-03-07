package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
    TalonSRX t = new TalonSRX(10);
    Joystick j = new Joystick(0);
    int target = 0;

    public void teleopInit() {
        //t.set(ControlMode.Position, 0.0);
        t.setSelectedSensorPosition(0);
        target = 0;
    }
    public void teleopPeriodic() {
        
        if (t.getSelectedSensorPosition() < (target-2000)) {
            t.set(ControlMode.PercentOutput, 0.3);
        }
        else if (t.getSelectedSensorPosition() > (target+2000)){
            t.set(ControlMode.PercentOutput, -0.3);
        }
        else {
            t.set(ControlMode.PercentOutput, 0);
        }
        if (j.getRawButtonPressed(1))
            target = 0;
        if (j.getRawButtonPressed(2))
            target = 80000;
        if (j.getRawButtonPressed(3))
            target = 160000;
        if (j.getRawButtonPressed(4))
            target = 260000;
        //t.setSelectedSensorPosition((int) Math.floor(100000*(j.getRawAxis(0))));
        System.out.println(t.getSelectedSensorPosition());
    }
}