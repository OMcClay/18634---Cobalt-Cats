package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

//NOT DONE
public class autoFunctions {
    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;

    private LinearOpMode opmode;
    private constants constant = new constants();

    public autoFunctions(LinearOpMode opmode) {
        this.opmode = opmode;
        HardwareMap hardwareMap = opmode.hardwareMap;
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");

        // Reverse your left side motor
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        // Note: You will likely need to reverse backLeftMotor here too!
    }
//    drivetrain robot = new drivetrain();
//    constants constant = new constants();
//    test2importing opmode = new test2importing();

    public boolean isDrivetrainBusy() {
        return frontRightMotor.isBusy() ||
                frontLeftMotor.isBusy() ||
                backRightMotor.isBusy() ||
                backLeftMotor.isBusy();
    }

    public void setTargetPosition(double distance, int fr, int fl, int br, int bl) {
        double counts = constant.COUNTS_PER_INCH;
        int target = (int) (distance * counts);
        frontRightMotor.setTargetPosition(fr * target);
        frontLeftMotor.setTargetPosition(fl * target);
        backRightMotor.setTargetPosition(br * target);
        backLeftMotor.setTargetPosition(bl * target);

    }

    public void setPower(double p) {
        frontRightMotor.setPower(Math.abs(p));
        frontLeftMotor.setPower(Math.abs(p));
        backRightMotor.setPower(Math.abs(p));
        backLeftMotor.setPower(Math.abs(p));
    }

    public void showCurrentPosition() {
        opmode.telemetry.addData("FR Position", frontRightMotor.getCurrentPosition());
        opmode.telemetry.addData("FL Position", frontLeftMotor.getCurrentPosition());
        opmode.telemetry.addData("BR Position", backRightMotor.getCurrentPosition());
        opmode.telemetry.addData("BL Position", backLeftMotor.getCurrentPosition());
    }

    public void setRunMode(DcMotor.RunMode mode) {
        frontLeftMotor.setMode(mode);
        frontRightMotor.setMode(mode);
        backLeftMotor.setMode(mode);
        backRightMotor.setMode(mode);
    }


    //    Driving Functions
    public void forward(double inches, double power) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setTargetPosition(inches, 1, 1, 1, 1);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);
        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
            showCurrentPosition();
            opmode.telemetry.update();
        }
        setPower(0);
        setRunMode((DcMotor.RunMode.RUN_USING_ENCODER));
    }

    public void backward(double inches, double power) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setTargetPosition(inches, -1, -1, -1, -1);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);
        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
            showCurrentPosition();
            opmode.telemetry.update();
        }
        setPower(0);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
//
    public void right(double inches, double power) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setTargetPosition(inches, -1, 1, 1, -1);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);
        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
            showCurrentPosition();
            opmode.telemetry.update();
        }
        setPower(0);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
//
    public void left(double inches, double power) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setTargetPosition(inches, 1, -1, -1, 1);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);
        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
            showCurrentPosition();
            opmode.telemetry.update();
        }
        setPower(0);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void turn_right(double degrees, double power) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        double inchesToTravel = (degrees / 360.0) * constant.turningCircumference;
        setTargetPosition(inchesToTravel, -1, 1, -1, 1);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);
        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
            showCurrentPosition();
            opmode.telemetry.update();
        }
        setPower(0);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
//
//    public void turn_left(double degrees, double power) {
//        robot.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        double inchesToTravel = (degrees / 360.0) * constant.turningCircumference;
//        setTargetPosition(inchesToTravel, 1, -1, 1, -1);
//        robot.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
//        setPower(power);
//        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
//            showCurrentPosition();
//            opmode.telemetry.update();
//        }
//        setPower(0);
//        robot.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }

    }

