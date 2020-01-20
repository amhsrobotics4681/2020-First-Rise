/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


public class Constants {

    // Joystick Buttons
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

    //Robot --- CHANGE NUMBERS TO BUTTON VARIABLES DEFINED ABOVE
    // petition for new naming convention: bXXXXX = button
    public static final int kClimberExtendButton = 1; //Redundant assignment currently
    public static final int kClimberContractButton = 2; //Redundant assignment currently
    public static final int kClimberStopButton = 3;
    public static final int kRotateSetColorButton = 4; //Redundant assignment currently
    public static final int kRotateRevolutionsButton = 5; //Redundant assignment currently
    public static final int kManualIntakeOverrideButton = 7;
    public static final int kManualShootingOverrideButton = 8;
    public static final int kAlignButton = 9;
    public static final int kLeftMotorInput = 0;//Redundant assignment currently
    public static final int kRightMotorInput = 1;//Redundant assignment currently
    public static final int kRaspberryPiXInput = 0;
    public static final int kRaspberryPiYInput = 1;
    public static final boolean automaticShootingAfterAligning = false;//Switch to true if you want to shoot immediately after lining up
    public static final double kDrivingSpeed = .4;



    //Climber
    public static final int kPulleyInput = 6;//Redundant assignment currently
    public static final int kWinchInput = 7;//Redundant assignment currently
}