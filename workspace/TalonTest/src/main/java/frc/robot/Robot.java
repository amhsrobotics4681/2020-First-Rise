package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.PWMTalonSRX;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
    TalonSRX t = new TalonSRX(0);

    public void robotInit() {
        t.set(ControlMode.PercentOutput, 60);
    }
}