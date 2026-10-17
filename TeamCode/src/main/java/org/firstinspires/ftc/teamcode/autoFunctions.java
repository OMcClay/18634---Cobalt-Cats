package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

//NOT DONE
public class autoFunctions {
    DcMotor frontRightMotor;
    DcMotor frontLeftMotor;
    DcMotor backRightMotor;
    DcMotor backLeftMotor;
    public autoFunctions(HardwareMap hardwareMap) {
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");

        // Reverse your left side motor
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        // Note: You will likely need to reverse backLeftMotor here too!
    }
    drivetrain robot = new drivetrain();
    constants constant = new constants();
    test2importing opmode = new test2importing();

    public boolean isDrivetrainBusy() {
        return robot.frontRightMotor.isBusy() ||
                robot.frontLeftMotor.isBusy() ||
                robot.backRightMotor.isBusy() ||
                robot.backLeftMotor.isBusy();
    }

    public void setTargetPosition(double distance) {
        double counts = constant.COUNTS_PER_INCH;
        int target = (int)(distance * counts);
        robot.frontRightMotor.setTargetPosition(target);
        robot.frontLeftMotor.setTargetPosition(target);
        robot.backRightMotor.setTargetPosition(target);
        robot.backLeftMotor.setTargetPosition(target);

    }

    public void setPower (double p) {
        robot.frontRightMotor.setPower(Math.abs(p));
        robot.frontLeftMotor.setPower(Math.abs(p));
        robot.backRightMotor.setPower(Math.abs(p));
        robot.backLeftMotor.setPower(Math.abs(p));
    }

    public void showCurrentPosition() {
        opmode.telemetry.addData("FR Position", robot.frontRightMotor.getCurrentPosition());
        opmode.telemetry.addData("FL Position", robot.frontLeftMotor.getCurrentPosition());
        opmode.telemetry.addData("BR Position", robot.backRightMotor.getCurrentPosition());
        opmode.telemetry.addData("BL Position", robot.backLeftMotor.getCurrentPosition());
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
        setTargetPosition(inches);
        robot.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);
        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
            showCurrentPosition();
            opmode.telemetry.update();
        }
        setPower(0);
        robot.setRunMode((DcMotor.RunMode.RUN_USING_ENCODER));
    }

//    public void backward(double inches, double power) {
//        robot.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        setTargetPosition(inches, -1, -1, -1, -1);
//        robot.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
//        setPower(power);
//        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
//            showCurrentPosition();
//            opmode.telemetry.update();
//        }
//        setPower(0);
//        robot.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
//
//    public void right(double inches, double power) {
//        robot.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        setTargetPosition(inches, -1, 1, 1, -1);
//        robot.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
//        setPower(power);
//        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
//            showCurrentPosition();
//            opmode.telemetry.update();
//        }
//        setPower(0);
//        robot.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
//
//    public void left(double inches, double power) {
//        robot.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        setTargetPosition(inches, 1, -1, -1, 1);
//        robot.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
//        setPower(power);
//        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
//            showCurrentPosition();
//            opmode.telemetry.update();
//        }
//        setPower(0);
//        robot.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
//    public void turn_right(double degrees, double power) {
//        robot.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        double inchesToTravel = (degrees / 360.0) * constant.turningCircumference;
//        setTargetPosition(inchesToTravel, -1, 1, -1, 1);
//        robot.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
//        setPower(power);
//        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
//            showCurrentPosition();
//            opmode.telemetry.update();
//        }
//        setPower(0);
//        robot.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
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
