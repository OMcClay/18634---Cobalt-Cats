package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


public class autoFunctions {
    public Servo gatey;

    ElapsedTime timer = new ElapsedTime();


    private LinearOpMode opmode;
    private robot robot;
    private constants constant = new constants();

    drivetrain driveTrain = new drivetrain();

    public autoFunctions(LinearOpMode opmode, robot robot) {
        this.opmode = opmode;
        this.robot = robot;
    }

    public boolean isDrivetrainBusy() {
        return robot.drive.frontRightMotor.isBusy() ||
                robot.drive.frontLeftMotor.isBusy() ||
                robot.drive.backRightMotor.isBusy() ||
                robot.drive.backLeftMotor.isBusy();
    }

    public void setTargetPosition(double distance, int fr, int fl, int br, int bl) {
        double counts = constant.COUNTS_PER_INCH;
        int target = (int) (distance * counts);
        robot.drive.frontRightMotor.setTargetPosition(fr * target);
        robot.drive.frontLeftMotor.setTargetPosition(fl * target);
        robot.drive.backRightMotor.setTargetPosition(br * target);
        robot.drive.backLeftMotor.setTargetPosition(bl * target);

    }

    public void setPower(double p) {
        robot.drive.frontRightMotor.setPower(Math.abs(p));
        robot.drive.frontLeftMotor.setPower(Math.abs(p));
        robot.drive.backRightMotor.setPower(Math.abs(p));
        robot.drive.backLeftMotor.setPower(Math.abs(p));
    }

    public void showCurrentPosition() {
        opmode.telemetry.addData("FR Position", robot.drive.frontRightMotor.getCurrentPosition());
        opmode.telemetry.addData("FL Position", robot.drive.frontLeftMotor.getCurrentPosition());
        opmode.telemetry.addData("BR Position", robot.drive.backRightMotor.getCurrentPosition());
        opmode.telemetry.addData("BL Position", robot.drive.backLeftMotor.getCurrentPosition());
    }

    public void setRunMode(DcMotor.RunMode mode) {
        robot.drive.frontLeftMotor.setMode(mode);
        robot.drive.frontRightMotor.setMode(mode);
        robot.drive.backLeftMotor.setMode(mode);
        robot.drive.backRightMotor.setMode(mode);
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

    public void turn_left(double degrees, double power) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        double inchesToTravel = (degrees / 360.0) * constant.turningCircumference;
        setTargetPosition(inchesToTravel, 1, -1, 1, -1);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);
        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
            showCurrentPosition();
            opmode.telemetry.update();
        }
        setPower(0);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void shoot(double shoot_v) {
        robot.operator.flyWheel.setVelocity(shoot_v);
        opmode.sleep(2800);
        timer.reset();
        while (opmode.opModeIsActive() && (timer.seconds()<4)){
            robot.operator.transfer.setPower(0.5);
            robot.operator.intake.setPower(0.5);
            opmode.telemetry.addData("Current position", robot.operator.flyWheel.getCurrentPosition());
            opmode.telemetry.update();
        }
        robot.operator.flyWheel.setVelocity(0);
        robot.operator.transfer.setPower(0);
        robot.operator.intake.setPower(0);
    }


    }

