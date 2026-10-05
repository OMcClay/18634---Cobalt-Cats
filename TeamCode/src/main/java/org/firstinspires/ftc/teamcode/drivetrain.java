package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

// UNDER CONSTRUCTION MUST BE FIXED

public class drivetrain {
    autoFunctions autoFunc = new autoFunctions();

    public DcMotor frontRightMotor = null;
    public DcMotor frontLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor backLeftMotor = null;

    // Hardware Map reference
    private HardwareMap hwMap = null;

    public drivetrain() {
        // Constructor left empty
    }

//  initialize
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        // Retrieve motors from Hardware Map configuration
        frontRightMotor = hwMap.get(DcMotor.class, "frontRight");
        frontLeftMotor  = hwMap.get(DcMotor.class, "frontLeft");
        backRightMotor  = hwMap.get(DcMotor.class, "backRight");
        backLeftMotor   = hwMap.get(DcMotor.class, "backLeft");

        // Set motor directions
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set brake behavior
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set initial run modes
        autoFunc.stopAndResetEncoders();
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Stop all motors
        setDrivePower(0, 0, 0, 0);
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


//Trying to put fieldcentric drive here:
//    public void setDrivePower(double )
}


