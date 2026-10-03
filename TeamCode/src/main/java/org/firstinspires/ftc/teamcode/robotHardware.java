package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

// UNDER CONSTRUCTION MUST BE FIXED

public class robotHardware {
    public DcMotor frontRightMotor = null;
    public DcMotor frontLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor backLeftMotor = null;

    // Encoder & Hardware Constants
    public static final double TICKS_PER_MOTOR_REV = 28.0;
    public static final double GEAR_REDUCTION = 18.88;
    public static final double WHEEL_DIAMETER_INCHES = 2.95276;
    public static final double COUNTS_PER_INCH = (TICKS_PER_MOTOR_REV * GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);

    // Hardware Map reference
    private HardwareMap hwMap = null;

    public robotHardware() {
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
        stopAndResetEncoders();
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Stop all motors
        setDrivePower(0, 0, 0, 0);
    }

    public void setRunMode(DcMotor.RunMode mode) {
        frontLeftMotor.setMode(mode);
        frontRightMotor.setMode(mode);
        backLeftMotor.setMode(mode);
        backRightMotor.setMode(mode);
    }
    public void stopAndResetEncoders() {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void setDrivePower(double fl, double fr, double bl, double br) {
        frontLeftMotor.setPower(fl);
        frontRightMotor.setPower(fr);
        backLeftMotor.setPower(bl);
        backRightMotor.setPower(br);
    }
}
