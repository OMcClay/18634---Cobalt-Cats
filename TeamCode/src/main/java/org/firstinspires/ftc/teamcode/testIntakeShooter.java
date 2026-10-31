package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "testShooter", group = "TeleOp")
public class testIntakeShooter extends LinearOpMode {
    private DcMotor frontRightMotor = null;
    private DcMotor frontLeftMotor = null;
    private DcMotor backRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor intake;
    private DcMotor transfer;
    private DcMotor flyWheel;
    drivetrain drivetrain;
    @Override

    public void runOpMode() throws InterruptedException {
        drivetrain = new drivetrain(this);

        waitForStart();

        if (opModeIsActive()) {
            flyWheel.setPower(0.5);
            intake.setPower(0.5);
            transfer.setPower(0.5);




        }
    }
}
