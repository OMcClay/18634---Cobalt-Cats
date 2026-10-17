package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

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

    double intakeSpeed = 0.75;
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


        waitForStart();
        while (opModeIsActive()) {
            botCentricDrive();
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
//        }

        telemetry.addData("gatePos", gate.getPosition());
        telemetry.addData("velocity", flyWheel.getVelocity());
        telemetry.addData("flyWheel", flyWheelStatus);
        telemetry.update();
    }
}