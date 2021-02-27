package frc.robot;

public class Test{
    int thisIsAVariable = -12;
    float anotherVariable = 427.6f;
    double betterVariable = 403.0;
    String wordVariable = "word";
    boolean fancyVariable = true;
    boolean condition = true;

    public Test(){

    }

    public void ifStatement(){
        //If the robot hasn't won yet, then the robot should win. Otherwise, if the robot did win, but barely, the robot should win more. Otherwise, the robot should celebrate.
        if(theRobotHasntWonYet){
            robot.win();
        } else if(didWinButBarely){
            robot.winMore();
        } else {
            robot.celebrate();
        }
    }

    public void operators(){
        int ten = 5 + 5;
        int three = 6 - 3;
        boolean notTrue = !true;
        boolean whatever = (6 == 7);
        boolean funANDStatements = ((5 == 6) && (6 < 10));
        boolean funORStatements = ((5 == 6) || (6 < 0));

        //thisIsAVariable = thisIsAVariable + 7;
        thisIsAVariable += 7;

        //thisIsAVariable += 1;
        thisIsAVariable ++;
    }

    public void pseudoCode(){
        //if I push the joystick forward
        if(controller.getAxis(1) > 0){
            robot.driveForward();
        }
        //then the robot should move forward
    }
}