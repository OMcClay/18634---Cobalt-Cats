package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
@Autonomous(name = "random_test2", group = "Autonomous")
public class test2importing extends LinearOpMode {

    autoFunctions autoFunc;

    @Override
    public void runOpMode() throws InterruptedException {
        autoFunc = new autoFunctions(this);

        double set_power = 0.5;

        waitForStart();

        if (opModeIsActive()) {
            autoFunc.forward(5.0, set_power);
            sleep(2000);
            autoFunc.backward(5.0, set_power);
            sleep(2000);
            autoFunc.right(5.0, set_power);
            sleep(2000);
            autoFunc.left(5.0, set_power);
            sleep(2000);
            autoFunc.turn_right(90.0, set_power);
            sleep(2000);

        }
    }
}