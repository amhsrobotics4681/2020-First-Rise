/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


public class Constants {

    // Joystick Buttons - private, so k____ unnecessary
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

    // PWM Declarations
    public static final int PWM_LeftTreads = 0;
    public static final int PWM_RightTreads = 1;
    public static final int PWM_Intake = 2;
    public static final int PWM_Indexer = 3;
    public static final int PWM_Shooter = 4;
    public static final int PWM_Spinner = 5;
    public static final int PWM_Pulley = 6;
    public static final int PWM_Winch = 7;

    // DIO Declarations
    public static final int DIO_IntakeSwitch = 0;

    // Joystick Buttons
    public static final int bExtendClimber = 1; //Redundant assignment currently
    public static final int bContractClimber = 2; //Redundant assignment currently
    public static final int bStopClimber = 3;
    public static final int bPositionControl = 4; //Redundant assignment currently
    public static final int bRotationControl = 5; //Redundant assignment currently
    public static final int bIntakeToggle = 7;
    public static final int bResetShooter = 8;
    public static final int bAlignRobot = 9;

    // Various Speed Constants
    public static final double kIntakeSpeed = 0.3; //arbitrary values
    public static final double kIndexSpeed = 0.3;
    public static final double kEjectionSpeed = 0.6;
    public static double kShooterSpeed = 0.8;
    public static final double kRotationSpeed = 0.5;
    public static final double kPositionSpeed = 0.1;
    public static final double kPulleySpeed = 1.0;
    public static final double kWinchSpeed = 1.0;
    public static final double kAligningSpeed = 0.4;

    // Various Robot Constants
    public static final int kRaspberryPiXInput = 0;
    public static final int kRaspberryPiYInput = 1;
    public static final boolean automaticShootingAfterAligning = false;
    //Switch to true if you want to shoot immediately after lining up
    // Going with Ryan-level name lengths, eh? ^^
}