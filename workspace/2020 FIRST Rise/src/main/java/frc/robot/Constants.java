/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * Add your docs here.
 */
public class Constants {
    
//PWM's
    public static final int PWM_TreadsLeft = 0;
    public static final int PWM_TreadsRight = 1;
    public static final int PWM_BallIntake = 2;
    public static final int PWM_BallIndexer = 3;
    public static final int PWM_BallShooter = 4;
    public static final int PWM_Wheel = 5;
    public static final int PWM_ClimberWinch = 6;
    public static final int PWM_ClimberPulley = 7;

//DIO's
    public static final int DIO_BallCounter = 0;
    public static final int DIO_LIDAR = 1;

//Buttons
    private static final int buttonX = 1;
    private static final int buttonA = 2;
    private static final int buttonB = 3;
    private static final int buttonY = 4;
    private static final int bumperL = 5;
    private static final int bumperR = 6;
    private static final int triggerL = 7;
    private static final int triggerR = 8;
    private static final int buttonBack = 9;
    private static final int buttonStart = 10;
    private static final int stickL = 11;
    private static final int stickR = 12;

//Button outputs
    public static final int bPositionControl = 1; //Redundant assignment currently
    public static final int bRotationControl = 2; //Redundant assignment currently
    public static final int bIntakeToggle = 3;
    public static final int bResetShooter = 4;
    public static final int bAlignRobot = 5;

// Speed declarations
    public static final double kIntakeSpeed = 0.3; //arbitrary values
    public static final double kIndexSpeed = 0.3;
    public static final double kEjectionSpeed = 0.6;
    public static double kShooterSpeed = 0.8;
    public static final double kRotationSpeed = 0.5;
    public static final double kPositionSpeed = 0.1;
    public static final double kPulleySpeed = 1.0;
    public static final double kWinchSpeed = 1.0;
    public static final double kAligningSpeed = 0.4;
}
