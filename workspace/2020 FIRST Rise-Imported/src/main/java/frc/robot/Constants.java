/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public class Constants {
    
//PWM's
    public static final int PWM_TreadsLeft = 0;
    public static final int PWM_TreadsRight = 1;
    public static final int PWM_BallIntake = 3;
    public static final int PWM_BallIndexer = 2;
    public static final int PWM_BallShooterL = 4;
    public static final int PWM_BallShooterR = 5;
    public static final int PWM_Wheel = 8; 
    public static final int PWM_ClimberPulley = 6;
    public static final int PWM_Servo = 7;

//CAN's
    public static final int CAN_Climber = 20;
    public static final int CAN_Screw = 10;

//Climber
    public static final int LowClimberHeight = 400000; //absolutely arbitraty, need to find actual values
    public static final int ClimberHeight = 600000; //absolutely arbitraty, need to find actual values
    public static final int HighClimberHeight = 800000; //absolutely arbitraty, need to find actual values
//DIO's
    public static final int DIO_BallSwitchFront = 0;
    public static final int DIO_BallSwitchBack = 2;
    public static final int DIO_LIDAR = 1;
    public static final int LidarError = 21;//Distance from shooting point to lidar
    public static final int DIO_ScrewSwitch = 3;

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

//Button outputs - controllerDriver
    public static final int bPositionControl = buttonX; 
    public static final int bRotationControl = buttonY; 
    public static final int bStopWheel = bumperR;
    public static final int bIntakeToggle = buttonA;
    public static final int bSpitToggle = buttonB;    
    public static final int bAlignRobot = buttonBack;
    public static final int bDriving = buttonStart;
    public static final int bResetScrew = stickR;
    public static final int bLoading = bumperL;

// Speed declarations
    public static final double kIntakeSpeed = -1; //(neg)
    public static final double kSpitSpeed = 1; //(not) neg
    public static final double kIndexSpeed = -0.6; // neg
    public static final double kEjectionSpeed = -0.5; // neg
    public static final double kShooterSpeed = 1; //was 0.8
    public static final double kAutonomousShooterSpeed = .6;
    public static final double kRotationSpeed = 0.5;
    public static final double kPositionSpeed = 0.2;
    public static final double kPulleySpeed = 0.4;
    public static final double kAligningSpeed = 0.4;
    public static final double kSpeedCurve = 0.04;
}
