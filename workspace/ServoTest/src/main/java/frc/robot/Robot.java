package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
  Servo s;
  Joystick j;

  @Override
  public void robotInit() {
      s = new Servo(3);
      j = new Joystick(0);
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
      System.out.println(s.getAngle());
      s.setPosition(j.getRawAxis(0)/2+.5);
    if (j.getRawButton(1)){
        //s.set(0);
        s.setPosition(0.30);
        System.out.println("1");
    }
    if (j.getRawButton(2)){
        s.setPosition(0.100);
        System.out.println("2");
    }
    if (j.getRawButton(3)){
        s.setPosition(0.50);
        System.out.println("3");
    }
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
