package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
//NOT DONE - FIX

@Autonomous(name = "random_test", group = "Autonomous")
public class test2importing extends LinearOpMode {

    drivetrain robot = new drivetrain();
    constants constant = new constants();
    autoFunctions autoFunc = new autoFunctions();

    double counts = constant.COUNTS_PER_INCH;


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

//        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
//        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
//        backRightMotor = hardwareMap.get(DcMotor.class, "backRight");
//        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
//        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
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
            autoFunc.turn_left(90.0, set_power);


        }
    }
}