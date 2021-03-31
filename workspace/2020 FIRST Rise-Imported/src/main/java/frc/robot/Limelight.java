package frc.robot;

import edu.wpi.first.networktables.*;

public class Limelight {
    private int LED_status;
    public double getX() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    }
    public boolean hasValidTarget() {
        return (NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0) > 0);
    }
    public double getY() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    }
    public void setLED(boolean state) {
        LED_status = state ? 3 : 1; // 3 is on, 1 is off
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(LED_status);
    }
    public boolean getLED() {
        return LED_status == 3;
    }
    public void setPipeline(int pipeline) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(pipeline);
    }
    public boolean isAligned() {
        return (Math.abs(getX()) < 3.5); // within 3.5 deg of target
    }
}
