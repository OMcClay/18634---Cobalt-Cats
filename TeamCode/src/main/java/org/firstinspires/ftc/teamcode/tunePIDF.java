package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
@TeleOp (name = "tunePIDF")
public class tunePIDF extends LinearOpMode {
    DcMotorEx testMotor;
    public static double highVelocity = 1300;
    double lowVelocity = 900;
    public static double curTargetVelocity = highVelocity;
    public static double tuneP = 90;
    public static double tuneI = 0;
    public static double tuneD = 9;
    public static double tuneF = 14.5;

    @Override
    public void runOpMode() throws InterruptedException{
        testMotor = hardwareMap.get(DcMotorEx.class, "flyWheel");
        testMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        testMotor.setDirection(DcMotorEx.Direction.FORWARD);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        DcMotor transfer = hardwareMap.get(DcMotor.class, "transfer");
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");

        intake.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();
        while (opModeIsActive()) {
            if(gamepad1.yWasPressed()) {
                if(curTargetVelocity == highVelocity) {
                    curTargetVelocity = lowVelocity;
                } else {
                    curTargetVelocity = highVelocity;
                }
            }

            transfer.setPower(gamepad1.left_stick_y);
            intake.setPower(gamepad1.left_stick_y);





            PIDFCoefficients pidfCoefficients = new PIDFCoefficients(tuneP, tuneI, tuneD, tuneF);
            testMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

            testMotor.setVelocity(curTargetVelocity);

            double curVelocity = testMotor.getVelocity();
            double error = curTargetVelocity - curVelocity;

            telemetry.addData("Target Velocity", curTargetVelocity);
            telemetry.addData("Actual Velocity", curVelocity);
            telemetry.addData("Velocity Error", error);
            telemetry.addData("P", tuneP);
            telemetry.addData("I", tuneI);
            telemetry.addData("D", tuneD);
            telemetry.addData("F", tuneF);
            telemetry.update();
        }
    }

    public void nonDashboardTune() {
        double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.001};
        int stepIndex = 1;

        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (gamepad1.dpadLeftWasPressed()) {
            tuneF -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadRightWasPressed()) {
            tuneF += stepSizes[stepIndex];
        }

        if (gamepad1.dpadUpWasPressed()) {
            tuneP += stepSizes[stepIndex];
        }
        if (gamepad1.dpadDownWasPressed()) {
            tuneP -= stepSizes[stepIndex];
        }
    }
}
