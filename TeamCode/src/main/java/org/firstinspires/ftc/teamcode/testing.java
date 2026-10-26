
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@TeleOp(name = "hi owen i think youre cool - alex")
public class testing extends LinearOpMode {
    private IMU imu;

    private Servo gateServo;

    private double cameraPosition = 0.5;

    @Override
    public void runOpMode() {
        gateServo = hardwareMap.get(Servo.class, "gate");
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        gateServo.setPosition(cameraPosition);
        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            gateServo.setPosition(gateServo.getPosition()+gamepad1.left_stick_x*0.002);
            telemetry.addData("Servo Current Position", gateServo.getPosition());
            sleep(5);
            telemetry.update();
        }
    }
}