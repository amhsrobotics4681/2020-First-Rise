package frc.robot;

import edu.wpi.first.networktables.*;

public class Limelight {
    public double getX() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    }
    public boolean getValidTarget() {
        return (NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0) > 0);
    }
    public double getY() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    }
    public void setLED(boolean state) {
        if (state)
            NetworkTableInstance.getDefault().getTable("limeLight").getEntry("ledMode").setNumber(3);
        else
            NetworkTableInstance.getDefault().getTable("limeLight").getEntry("ledMode").setNumber(1);
    }
    public void setPipeline(int pipeline) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(pipeline);
    }
}
