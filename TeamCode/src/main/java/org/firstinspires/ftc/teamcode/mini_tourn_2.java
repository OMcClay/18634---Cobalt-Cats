package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "park_mini_tournament", group = "Autonomous")
public class mini_tourn_2 extends LinearOpMode {

    autoFunctions autoFunc;

    @Override
    public void runOpMode() throws InterruptedException {
        autoFunc = new autoFunctions(this);
        double set_power = 0.5;

        waitForStart();

        if (opModeIsActive()) {
            autoFunc.forward(4.0, set_power);
            autoFunc.left(10.0, set_power);

//            autoFunc.left(36.0, set_power);
//            autoFunc.forward(26.0, set_power);
//            autoFunc.turn_right(90.0, set_power);
//            autoFunc.backward(20.0, set_power);
//            //INTAKE from flower
//            autoFunc.forward(20.0, set_power);
//            autoFunc.left(74.0, set_power);
//            autoFunc.forward(36.0, set_power);
//            autoFunc.turn_right(90.0, set_power);
//            //SHOOT
//            autoFunc.right(12.0, set_power);
//            autoFunc.backward(18.0, set_power);
//            autoFunc.forward(18.0, set_power);
//            autoFunc.left(12.0, set_power);
//            //SHOOT
//            autoFunc.right(60.0, set_power);
//            autoFunc.forward(12.0, set_power);

        }
    }
}