package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import com.analog.adis16470.frc.ADIS16470_IMU;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Counter;

public class Robot extends TimedRobot {
    double kAngle = 0.0;
    double dps = -0.001; // drift per second
    DifferentialDrive m_drive = new DifferentialDrive(
        new Victor(1), new Victor(2));
    ADIS16470_IMU imu = new ADIS16470_IMU();
    Joystick m_stick = new Joystick(0);
    int timer;
    Counter counter;
    double offSetIn = 0;
    double cumulative = 0;

    @Override
    public void robotInit() {
        //imu.calibrate();
        // after robot is turned on, don't enable immediately
        counter = new Counter(0);
        counter.setMaxPeriod(1.0);
        counter.setSemiPeriodMode(true);
        counter.reset();
    }
    public double getDistance() {
        if (counter.get() < 1) {
           //return 0.0;
        }
        // getPeriod returns a value of 10 usec / cm.
        double rawPeriod = counter.getPeriod();
        double distanceCm = rawPeriod * 100000;
        double distanceIn = distanceCm / 2.54;
        //System.out.println("LIDAR: " + distanceCm + " , " + distanceIn);
        return distanceIn + offSetIn;
    }

    @Override
    public void teleopInit() {
        timer = 0;
        imu.calibrate();
    }

    @Override
    public void teleopPeriodic() {
        timer++;
        cumulative += getDistance();
        //System.out.println(getDistance());
        //m_drive.arcadeDrive(m_stick.getRawAxis(3),)
        if (timer%10==0) {
            System.out.println((Double.toString(cumulative / 10)).substring(0,7));
            cumulative= 0;
        }
        m_drive.tankDrive(m_stick.getRawAxis(1),
                          m_stick.getRawAxis(3));
        /*if (m_stick.getRawButtonPressed(5)) {
            timer = 0;
            imu.calibrate();
        }
        SmartDashboard.putNumber("theta: ", imu.getAngle()+dps*.02*timer);
        //imu.get
        */
    }
}