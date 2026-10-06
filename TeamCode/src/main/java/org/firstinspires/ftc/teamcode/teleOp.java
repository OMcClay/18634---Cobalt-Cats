package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp (name = "TeleOp")
public class teleOp extends LinearOpMode {
    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;
    DcMotor intake;
    DcMotor transfer;
    DcMotor flyWheel;


    @Override
    public void runOpMode() throws InterruptedException {
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
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
}