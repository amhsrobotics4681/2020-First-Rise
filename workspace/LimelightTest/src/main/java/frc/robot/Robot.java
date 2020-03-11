/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  DifferentialDrive m_drive;
  Joystick j = new Joystick(0);
    Victor m_intake = new Victor(3);
    Victor m_indexer = new Victor(2);
    Victor m_shooter = new Victor(4);
    Victor m_s = new Victor(5);
    int P,I,D = 1;
    double previousError = 0;
    double integral, previous_error, setpoint, derivative = 0;

    double error;

  @Override
  public void robotInit() {
    Victor m_left = new Victor(0);
    Victor m_right = new Victor(1);
    m_drive = new DifferentialDrive(m_left, m_right);
    m_right.setInverted(false);
    m_left.setInverted(true);
    
}

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
      
      


}

  @Override
  public void teleopPeriodic() {
    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    if (j.getRawButton(2)) {
        if (tv==1){
        System.out.println(PID());    
        m_drive.arcadeDrive(PID()*.05, 0); //0.64*Math.atan(0.2*ty));
        } else
            m_drive.arcadeDrive(.5,0.0);
    } else {
        m_drive.arcadeDrive(j.getRawAxis(0), 0);
    }
    if (j.getRawButton(5)) {
        m_intake.set(-.7);
        m_indexer.set(-.7);
    } else {
        m_intake.set(0);
        m_indexer.set(0);
    }
    if (j.getRawButton(6)) {
        m_shooter.set(.8);
        m_s.set(.8);
    } else {
        m_shooter.set(0);
        m_s.set(0);
    }
    
}

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }
  public double PID(){
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);  
    error = (setpoint - tx)*.4;
    integral += error*.02;
    derivative = (previousError - error)/.1;
    previousError = error;
    System.out.println("Error " + error + " Integral " + integral);
    return (error + integral + derivative);
  }
}
