package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
@Autonomous(name = "autoMeet1", group = "Autonomous")

public class autoMeet1 extends LinearOpMode{
    autoFunctions autoFunc;

    @Override
    public void runOpMode() throws InterruptedException {
        autoFunc = new autoFunctions(this);

        double set_power = 0.5;

        waitForStart();

        if (opModeIsActive()) {

            autoFunc.forward(22.0, set_power);
            //SHOOT
            autoFunc.right(45.0, set_power);
        }
    }
}

