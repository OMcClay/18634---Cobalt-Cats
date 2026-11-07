package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "shoot_auto", group = "Autonomous")
public class shootAuto extends LinearOpMode {
    robot robot = new robot(); // Instantiates your structural files
    autoFunctions autoFunc;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        autoFunc = new autoFunctions(this, robot);
        double set_power = 0.5;

        waitForStart();

        if (opModeIsActive()) {
            robot.operator.gate.setPosition(robot.operator.openGate);
            autoFunc.forward(4.0, set_power);
            autoFunc.shoot(1200);
            autoFunc.left(36.0, set_power);
            autoFunc.forward(73.0, set_power);
            autoFunc.left(27.0, set_power);
        
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