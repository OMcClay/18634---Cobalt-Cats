package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
//NOT DONE - FIX

@Autonomous(name = "random_test", group = "Autonomous")
public class test2importing extends LinearOpMode {

    drivetrain robot = new drivetrain();
    constants constant = new constants();
    autoFunctions autoFunc = new autoFunctions();

    double counts = constant.COUNTS_PER_INCH;


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

//        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
//        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
//        backRightMotor = hardwareMap.get(DcMotor.class, "backRight");
//        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
//        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        double set_power = 0.5;

        waitForStart();
        if (opModeIsActive()) {
            autoFunc.forward(3.0, set_power);
            sleep(2000);
            backward(3.0, set_power);


        }
    }


    public void right(double inches, double power) {
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target = (int)(inches * COUNTS_PER_INCH);
        frontRightMotor.setTargetPosition(-target);
        frontLeftMotor.setTargetPosition(target);
        backRightMotor.setTargetPosition(target);
        backLeftMotor.setTargetPosition(-target);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setPower(Math.abs(power));
        frontLeftMotor.setPower(Math.abs(power));
        backRightMotor.setPower(Math.abs(power));
        backLeftMotor.setPower(Math.abs(power));
        while (opModeIsActive() && (frontRightMotor.isBusy() || frontLeftMotor.isBusy() || backRightMotor.isBusy() || backLeftMotor.isBusy())) {
            telemetry.addData("FR Position", frontRightMotor.getCurrentPosition());
            telemetry.addData("FL Position", frontLeftMotor.getCurrentPosition());
            telemetry.addData("BR Position", backRightMotor.getCurrentPosition());
            telemetry.addData("BL Position", backLeftMotor.getCurrentPosition());
            telemetry.update();
        }
        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void left(double inches, double power) {
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target = (int)(inches * COUNTS_PER_INCH);
        frontRightMotor.setTargetPosition(target);
        frontLeftMotor.setTargetPosition(-target);
        backRightMotor.setTargetPosition(-target);
        backLeftMotor.setTargetPosition(target);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setPower(Math.abs(power));
        frontLeftMotor.setPower(Math.abs(power));
        backRightMotor.setPower(Math.abs(power));
        backLeftMotor.setPower(Math.abs(power));
        while (opModeIsActive() && (frontRightMotor.isBusy() || frontLeftMotor.isBusy() || backRightMotor.isBusy() || backLeftMotor.isBusy())) {
            telemetry.addData("FR Position", frontRightMotor.getCurrentPosition());
            telemetry.addData("FL Position", frontLeftMotor.getCurrentPosition());
            telemetry.addData("BR Position", backRightMotor.getCurrentPosition());
            telemetry.addData("BL Position", backLeftMotor.getCurrentPosition());
            telemetry.update();
        }
        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void turn_right(double inches, double power) {
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target = (int)(inches * COUNTS_PER_INCH);
        frontRightMotor.setTargetPosition(-target);
        frontLeftMotor.setTargetPosition(target);
        backRightMotor.setTargetPosition(-target);
        backLeftMotor.setTargetPosition(target);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setPower(Math.abs(power));
        frontLeftMotor.setPower(Math.abs(power));
        backRightMotor.setPower(Math.abs(power));
        backLeftMotor.setPower(Math.abs(power));
        while (opModeIsActive() && (frontRightMotor.isBusy() || frontLeftMotor.isBusy() || backRightMotor.isBusy() || backLeftMotor.isBusy())) {
            telemetry.addData("FR Position", frontRightMotor.getCurrentPosition());
            telemetry.addData("FL Position", frontLeftMotor.getCurrentPosition());
            telemetry.addData("BR Position", backRightMotor.getCurrentPosition());
            telemetry.addData("BL Position", backLeftMotor.getCurrentPosition());
            telemetry.update();
        }
        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void turn_left(double inches, double power) {
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target = (int)(inches * COUNTS_PER_INCH);
        frontRightMotor.setTargetPosition(target);
        frontLeftMotor.setTargetPosition(-target);
        backRightMotor.setTargetPosition(target);
        backLeftMotor.setTargetPosition(-target);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setPower(Math.abs(power));
        frontLeftMotor.setPower(Math.abs(power));
        backRightMotor.setPower(Math.abs(power));
        backLeftMotor.setPower(Math.abs(power));
        while (opModeIsActive() && (frontRightMotor.isBusy() || frontLeftMotor.isBusy() || backRightMotor.isBusy() || backLeftMotor.isBusy())) {
            telemetry.addData("FR Position", frontRightMotor.getCurrentPosition());
            telemetry.addData("FL Position", frontLeftMotor.getCurrentPosition());
            telemetry.addData("BR Position", backRightMotor.getCurrentPosition());
            telemetry.addData("BL Position", backLeftMotor.getCurrentPosition());
            telemetry.update();
        }
        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}