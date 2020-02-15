/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public class Constants {
    
//PWM's
    public static final int PWM_TreadsLeft = 2;// 
    public static final int PWM_TreadsRight = 1;
    public static final int PWM_BallIntake = 3;
    public static final int PWM_BallIndexer = 4;//
    public static final int PWM_BallShooterL = 7;
    public static final int PWM_BallShooterR = 8;
    public static final int PWM_Wheel = 5; 
    public static final int PWM_ClimberPulley = 9;
    public static final int PWM_Screw = 6;

//DIO's
    public static final int DIO_BallCounter = 9;
    public static final int DIO_LIDAR = 0;

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
    public static final int bPositionControl = buttonX; //Redundant assignment currently
    public static final int bRotationControl = buttonY; //Redundant assignment currently
    public static final int bIntakeToggle = buttonA;
    public static final int bIndexToggle = buttonB;
    public static final int bResetShooter = triggerR;
    public static final int bAlignRobot = triggerL;
    public static final int bToggleWheel = bumperR;
    public static final int bKillShooter = bumperL;
    public static final int bBallCountUp = buttonStart; //New idea - Eric
    public static final int bBallCountDown = buttonBack; //If you don't like it, I'll remove it

// Speed declarations
    public static final double kIntakeSpeed = -0.5; //arbitrary values
    public static final double kIndexSpeed = -0.5;
    public static final double kEjectionSpeed = -0.6;
    public static final double kShooterSpeed = -0.6;
    public static final double kRotationSpeed = 0.5;
    public static final double kPositionSpeed = 0.5;
    public static final double kPulleySpeed = 1.0;
    public static final double kWinchSpeed = 1.0;
    public static final double kAligningSpeed = 0.4;
    public static final double kSpeedCurve = 0.05;
}
