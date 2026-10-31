package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "testShooter", group = "TeleOp")
public class testIntakeShooter extends LinearOpMode {
    private DcMotor frontRightMotor = null;
    private DcMotor frontLeftMotor = null;
    private DcMotor backRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor intake;
    private DcMotor transfer;
    private DcMotor flyWheel;
    private ElapsedTime runTime = new ElapsedTime();
    autoFunctions autoFunc;
    @Override

    public void runOpMode() throws InterruptedException {
        autoFunc = new autoFunctions(this);

        waitForStart();

        if (opModeIsActive()) {

            intake.setPower(0.5);

            runTime.reset();

            flyWheel.setPower(0.8);

            while (opModeIsActive() && (runTime.seconds() < 3.5)) {
                telemetry.addData("status", "shooting");
                transfer.setPower(0.5);
                telemetry.addData("Elapsed Time", "%.2f / 2.50 s", runTime.seconds());
                telemetry.update();
            }
            flyWheel.setPower(0);
            transfer.setPower(0);
            intake.setPower(0);

            sleep(2000);

            //Function basicShoot
            autoFunc.basicShoot();






        }
    }
}
