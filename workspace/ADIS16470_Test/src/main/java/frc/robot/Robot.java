package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import com.analog.adis16470.frc.ADIS16470_IMU;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
    double kAngle = 0.0;
    double dps = 0.064; // drift per second
    DifferentialDrive m_drive = new DifferentialDrive(
        new Victor(1), new Victor(2));
    ADIS16470_IMU imu = new ADIS16470_IMU();
    Joystick m_stick = new Joystick(0);
    int timer;

    @Override
    public void robotInit() {
        imu.calibrate();
        // after robot is turned on, don't enable immediately
    }

    @Override
    public void teleopInit() {
        timer = 0;
        imu.calibrate();
    }

    @Override
    public void teleopPeriodic() {
        timer++;
        //m_drive.arcadeDrive(m_stick.getRawAxis(3),)
        if (timer%20==0)
            System.out.println(imu.getAngle());
        m_drive.tankDrive(m_stick.getRawAxis(1),
                          m_stick.getRawAxis(3));
        if (m_stick.getRawButtonPressed(5)) {
            timer = 0;
            imu.calibrate();
        }
        SmartDashboard.putNumber("theta: ", imu.getAngle()+dps*.02*timer);
        //imu.get
    }
}