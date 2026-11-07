package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "wait_shoot_auto", group = "Autonomous")
public class waitShootAuto extends LinearOpMode{
    robot robot = new robot(); // Instantiates your structural files
    autoFunctions autoFunc;
    ElapsedTime timer = new ElapsedTime();
    @Override

    public void runOpMode() throws InterruptedException{
        robot.init(hardwareMap);
        autoFunc = new autoFunctions(this, robot);
        double set_power = 0.5;

        waitForStart();

        if (opModeIsActive()) {
            sleep(10000);

            autoFunc.forward(4.0, set_power);
            autoFunc.shoot(1200);
            autoFunc.left(10.0, set_power);

        }
    }
}
