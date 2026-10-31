package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "autoMeet1", group = "Autonomous")

public class autoMeet1Basic extends LinearOpMode{
    autoFunctions autoFunc;

    @Override
    public void runOpMode() throws InterruptedException {
        autoFunc = new autoFunctions(this);

        double set_power = 0.5;

        waitForStart();

        if (opModeIsActive()) {

            autoFunc.forward(5.0, set_power);
            autoFunc.basicShoot();
            autoFunc.left(45.0, set_power);
        }
    }
}

