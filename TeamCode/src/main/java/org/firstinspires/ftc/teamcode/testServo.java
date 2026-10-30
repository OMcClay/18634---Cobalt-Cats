package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp (name = "Test Servo")
public class testServo extends LinearOpMode{

    Servo testServo;
    @Override
    public void runOpMode() throws InterruptedException {
        // gate, leftFlower, rightFlower
//        testServo = hardwareMap.get(Servo.class, "gate");
//        testServo = hardwareMap.get(Servo.class, "leftFlower");
        testServo = hardwareMap.get(Servo.class, "rightFlower");
        float i = 0f;

        waitForStart();
        while(opModeIsActive()) {
            if (gamepad1.right_bumper) {
                if (i != 1) {
                    i += 0.1f;
                }
                testServo.setPosition(i);
                telemetry.addData("Servo Position", testServo.getPosition());
                telemetry.addData("i", i);
                telemetry.update();
                sleep(500);
            } else if (gamepad1.left_bumper) {
                if (i != 0) {
                    i -= 0.1f;
                }
                testServo.setPosition(i);
                telemetry.addData("Servo Position", testServo.getPosition());
                telemetry.addData("i", i);
                telemetry.update();
                sleep(500);
            }

        }
    }
}