package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "testShooter", group = "TeleOp")
public class testIntakeShooter extends LinearOpMode {

    // 1. Declare variables at the top (without assigning them yet)
    private DcMotor intake;
    private DcMotor transfer;
    private DcMotor flyWheel;
    private ElapsedTime runTime = new ElapsedTime();
    autoFunctions autoFunc;

    @Override
    public void runOpMode() throws InterruptedException {

        // 2. Initialize them here, inside runOpMode, where hardwareMap is fully active
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        flyWheel = hardwareMap.get(DcMotor.class, "flyWheel");
        intake.setDirection(DcMotor.Direction.REVERSE);
        autoFunc = new autoFunctions(this);

        waitForStart();

        if (opModeIsActive()) {

            intake.setPower(0.5);
            runTime.reset();
            flyWheel.setPower(0.9);

            while (opModeIsActive() && (runTime.seconds() < 3.5)) {
                telemetry.addData("status", "shooting");
                transfer.setPower(0.5);
                // Tip: Updated your string formatting below to match your actual 3.5s limit
                telemetry.addData("Elapsed Time", "%.2f / 3.50 s", runTime.seconds());
                telemetry.update();
            }

            flyWheel.setPower(0);
            transfer.setPower(0);
            intake.setPower(0);

            sleep(2000);

            autoFunc.basicShoot();
        }
    }
}
