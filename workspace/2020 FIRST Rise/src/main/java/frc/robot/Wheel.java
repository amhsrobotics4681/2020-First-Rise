/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.I2C;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

public class Wheel {
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();
    private Victor m_motor;
    // below kColors have been tested in portables, proximity = 33
    private final Color kBlue = ColorMatch.makeColor(0.16, 0.44, 0.38);
    private final Color kGreen = ColorMatch.makeColor(0.21, 0.52, 0.26);
    private final Color kRed = ColorMatch.makeColor(0.43, 0.39, 0.17);
    private final Color kYellow = ColorMatch.makeColor(0.30, 0.54, 0.14);
    private String status = "Stationary";
    private String targetColor = "Green"; //Abitrary selection, set to whatever needed
    private final int kTargetRevolutions = 4;
    private String previousColor;
    private String currentColor;
    private int numPanelShifted;
    private int numPanelShiftNeeded;

    public void wheelInit() {
        m_colorMatcher.addColorMatch(kBlue);
        m_colorMatcher.addColorMatch(kGreen);
        m_colorMatcher.addColorMatch(kRed);
        m_colorMatcher.addColorMatch(kYellow);
        m_motor = new Victor(5); 
        currentColor = getColor();
    }
        
    /**Gets the color that is currently being detected*/
    public String getColor() {
        Color detectedColor = m_colorSensor.getColor();
        String colorString;
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        if (match.color == kBlue) {
            colorString = "Blue";
        } else if (match.color == kGreen) {
            colorString = "Green";
        } else if (match.color == kRed) {
            colorString = "Red";
        } else if (match.color == kYellow) {
            colorString = "Yellow";
        } else {
            colorString = "Unknown";
        }

        return colorString;
    }
    public void rotationControl(){
        if (!status.equals("Rotation")){
            status = "Rotation";
            numPanelShiftNeeded = 8*kTargetRevolutions;
            numPanelShifted = 0;
            m_motor.set(Constants.kRotationSpeed);
        }
        
    }
    public void positionControl(){
        status = "Position";
        m_motor.set(.1);

    }
    public void mainMethod() {
        if (status.equals("Rotation")){
            if (numPanelShifted < numPanelShiftNeeded){
                previousColor = currentColor;
                currentColor = getColor();
                if (!currentColor.equals(previousColor)){
                    numPanelShifted ++;
                    System.out.println(numPanelShifted);
                }
            }
            else {
                m_motor.set(0);
                status = "Stationary";
            }
        }
        if (status.equals("Position")){
            if (targetColor.equals(getColor())){
                m_motor.set(0);
                status = "Stationary";
            }
        }
    }
    public double getRed() {
        return m_colorSensor.getColor().red;
    }
    public double getBlue() {
        return m_colorSensor.getColor().blue;
    }
    public double getGreen() {
        return m_colorSensor.getColor().green;
    }
    public double getConfidence() {
        return m_colorMatcher.matchClosestColor(m_colorSensor.getColor()).confidence;
    }
    public double getProximity() {
        return m_colorSensor.getProximity();
    }
}
