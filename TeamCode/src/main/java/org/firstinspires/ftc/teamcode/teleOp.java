package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "TeleOp")
public class teleOp extends LinearOpMode {
    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;
    DcMotor intake;
    DcMotor transfer;
    DcMotor flyWheel;
    Servo gate;

    double intakeSpeed = 0.75;


    @Override
    public void runOpMode() throws InterruptedException {
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        gate = hardwareMap.get(Servo.class, "gate");
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        flyWheel = hardwareMap.get(DcMotor.class, "flyWheel");


        waitForStart();
        while (opModeIsActive()) {
            botCentricDrive();
        }
    }

    public void botCentricDrive(){
        fl.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
        fr.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
        bl.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
        br.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
    }

    public void fieldCentricDrive() {

    }

    public void operator() {
        final float closeGate = 1f;
        final float openGate = 0f;
        intakeSpeed = (0.75 + gamepad2.left_stick_y);
        intake.setPower(gamepad2.left_trigger * intakeSpeed);
        transfer.setPower(gamepad2.left_stick_y * intakeSpeed);

        if (gamepad2.triangle && flyWheel.getPower() == 0) {
            flyWheel.setPower(0.75);
        }
        else if(gamepad2.triangle && flyWheel.getPower() > 0) {
            flyWheel.setPower(0);
        }

        if (gamepad2.right_bumper && gate.getPosition() == closeGate) {
            gate.setPosition(openGate);
        }
        else if (gamepad2.right_bumper && gate.getPosition() == openGate) {
            gate.setPosition(closeGate);
        }
    }


}