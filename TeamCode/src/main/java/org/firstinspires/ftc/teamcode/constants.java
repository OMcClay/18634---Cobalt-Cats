package org.firstinspires.ftc.teamcode;

public class constants {
//    Speed thresholds
    public static final double DRIVE_SPEED_FAST = 0.8;
    public static final double DRIVE_SPEED_SLOW = 0.3;

//    Auto calculation attributes
    public static final double TICKS_PER_MOTOR_REV = 28.0;
    public static final double GEAR_REDUCTION = 18.88;
    public static final double WHEEL_DIAMETER_INCHES = 2.95276;
    public static final double COUNTS_PER_INCH = (TICKS_PER_MOTOR_REV * GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);


}
