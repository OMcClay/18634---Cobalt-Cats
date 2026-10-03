package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.external.samples.externalhardware.RobotHardware;

@Autonomous(name = "random_test", group = "Autonomous")
public class test1 extends LinearOpMode {
//    robotHardware robot = new robotHardware();
    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;
//
    static final double TICKS_PER_MOTOR_REV = 28.0;
    static final double GEAR_REDUCTION = 18.88;
    static final double WHEEL_DIAMETER_INCHES = 2.95276;
    static final double COUNTS_PER_INCH = (TICKS_PER_MOTOR_REV * GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);


    @Override
    public void runOpMode() throws InterruptedException {
//        robot.init(hardwareMap);
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        double set_power = 0.5;

        waitForStart();
        if (opModeIsActive()) {
            forward(3.0, set_power);
            sleep(2000);
            backward(3.0, set_power);


        }
    }
    public void forward(double inches, double power) {
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target = (int)(inches * COUNTS_PER_INCH);
        frontRightMotor.setTargetPosition(target);
        frontLeftMotor.setTargetPosition(target);
        backRightMotor.setTargetPosition(target);
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
    public void backward(double inches, double power) {
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target = (int)(inches * COUNTS_PER_INCH);
        frontRightMotor.setTargetPosition(-target);
        frontLeftMotor.setTargetPosition(-target);
        backRightMotor.setTargetPosition(-target);
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