package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp (name = "testFieldCentric", group="Linear Opmode")

public class testFieldCentric extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException{
        DcMotor frontLeftMotor  = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backLeftMotor   = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRightMotor  = hardwareMap.get(DcMotor.class, "backRight");
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //IMU
        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);


        waitForStart();
        imu.resetYaw();

        while (opModeIsActive()) {


            double y = -(gamepad1.left_stick_y);
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x; //rx is right x

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();

            telemetry.addData("Yaw (Heading)", "%.1f deg", orientation.getYaw(AngleUnit.DEGREES));
            telemetry.addData("Pitch", "%.1f deg", orientation.getPitch(AngleUnit.DEGREES));
            telemetry.addData("Roll", "%.1f deg", orientation.getRoll(AngleUnit.DEGREES));

            telemetry.update();

            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading); //rot is rotation
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower  = (rotY + rotX + rx) / denominator;
            double backLeftPower   = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower  = (rotY + rotX - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);



        }

    }
}
