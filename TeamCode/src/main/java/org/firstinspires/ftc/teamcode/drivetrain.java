package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

// UNDER CONSTRUCTION MUST BE FIXED

public class drivetrain {


    private DcMotor frontRightMotor = null;
    private DcMotor frontLeftMotor = null;
    private DcMotor backRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor intake;
    private DcMotor transfer;
    private DcMotor flyWheel;
    private LinearOpMode opmode;


    public drivetrain(LinearOpMode opmode) {
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


//    Drivetrain functions

    //Sets runMode for motors
    public void setRunMode(DcMotor.RunMode mode) {
        frontLeftMotor.setMode(mode);
        frontRightMotor.setMode(mode);
        backLeftMotor.setMode(mode);
        backRightMotor.setMode(mode);
    }

    //Sets power to motors
    public void setDrivePower(double fl, double fr, double bl, double br) {
        frontLeftMotor.setPower(fl);
        frontRightMotor.setPower(fr);
        backLeftMotor.setPower(bl);
        backRightMotor.setPower(br);
    }
}

    //  initialize


//    public void init(HardwareMap ahwMap) {
//        hwMap = ahwMap;
//
//        // Retrieve motors from Hardware Map configuration
//        frontRightMotor = hwMap.get(DcMotor.class, "frontRight");
//        frontLeftMotor  = hwMap.get(DcMotor.class, "frontLeft");
//        backRightMotor  = hwMap.get(DcMotor.class, "backRight");
//        backLeftMotor   = hwMap.get(DcMotor.class, "backLeft");
//
//        // Set motor directions
//        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
//
//        // Set brake behavior
//        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//        // Set initial run modes
//        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        // Stop all motors
//        setDrivePower(0, 0, 0, 0);
//    }



