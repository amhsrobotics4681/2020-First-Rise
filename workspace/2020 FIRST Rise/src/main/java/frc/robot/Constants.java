/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


public class Constants{


    //BallSystem
    public static double kIntakeSpeed = .3; //arbitrary value
    public static final double kWheelSpeed = .3; //arbitrary value
    public static final double kWheelShootingSpeed = .6;//arbitrary value
    public static final double kShooterSpeed = .8;//arbitrary value
    public static final int kDIOLimitSwitchWheelInput = 3;
    public static final int kDIOLimitSwitchWheelShooter = 4;




    //Robot
    public static final int kClimberExtendButton = 1; //Redundant assignment currently
    public static final int kClimberContractButton = 2; //Redundant assignment currently
    public static final int kClimberStopButton = 3;
    public static final int kRotateSetColorButton = 4; //Redundant assignment currently
    public static final int kRotateRevolutionsButton = 5; //Redundant assignment currently
    public static final int kManualWheelOverrideButton = 6;
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