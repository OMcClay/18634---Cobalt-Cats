package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp (name = "TeleOp")
public class teleOp extends LinearOpMode {
    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;
    DcMotor intake;
    DcMotor transfer;
    DcMotorEx flyWheel;
    Servo gate;
    IMU imu;

    double intakeSpeed = 0.75;
    double driveSpeed = 1;
    boolean flyWheelStatus = false;


    @Override
    public void runOpMode() throws InterruptedException {
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        gate = hardwareMap.get(Servo.class, "gate");
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");
        br.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        waitForStart();
        while (opModeIsActive()) {
            fieldCentricDrive();
            operator();
        }
    }

    public void botCentricDrive(){
        fl.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
        fr.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);
        bl.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);
        br.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);
    }

    public void fieldCentricDrive() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double pivot = gamepad1.right_stick_x;

        if (gamepad1.start) {
            imu.resetYaw();
        }

        double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        if (gamepad1.right_bumper) {
            if (driveSpeed == 1) {
                driveSpeed = 0.45;
            }
            else {
                driveSpeed = 1;
            }
        }

        double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
        double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);
        rotX *= 1.1;
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(pivot), 1.0);
        double frontLeftPower = -(rotY + rotX + pivot) / denominator;
        double backLeftPower = -(rotY - rotX + pivot) / denominator;
        double frontRightPower = -(rotY - rotX - pivot) / denominator;
        double backRightPower = -(rotY + rotX - pivot) / denominator;
        fl.setPower(frontLeftPower * driveSpeed);
        bl.setPower(backLeftPower * driveSpeed);
        fr.setPower(frontRightPower * driveSpeed);
        br.setPower(backRightPower * driveSpeed);
        telemetry.update();
    }

    public void operator() {
        final float closeGate = 0.5f;
        final float openGate = 0.25f;

        if (gamepad2.left_stick_y != 0){
            intakeSpeed = (0.75 * (-gamepad2.left_stick_y));
        }
        else {
            intakeSpeed = (0.75);
        }

        intake.setPower(gamepad2.left_trigger * intakeSpeed);
        transfer.setPower(gamepad2.left_trigger * intakeSpeed);

        if (gamepad2.triangle && !flyWheelStatus) {
            flyWheel.setPower(0.75);
            flyWheelStatus = true;
        }
        else if(gamepad2.triangle && flyWheelStatus) {
            flyWheel.setPower(0);
            flyWheelStatus = false;
        }

//        if (gamepad2.rightBumperWasPressed() && gate.getPosition() == closeGate) {
//            gate.setPosition(openGate);
//        }
//        else if (gamepad2.rightBumperWasPressed() && gate.getPosition() == openGate) {
//            gate.setPosition(closeGate);
//        }
        if (gamepad2.right_bumper) {
            gate.setPosition(0.3);
//            gate.getPosition() + 0.1
        }
//        for (int i = 0; i<=1; i += 0.1) {
//            gate.setPosition(i);
//            if (i >= 1) {
//                i = 0;
//            }
//            sleep(500);
//        }

        telemetry.addData("gatePos", gate.getPosition());
        telemetry.addData("velocity", flyWheel.getVelocity());
        telemetry.addData("flyWheel", flyWheelStatus);
        telemetry.update();
    }
}