package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class initOperator {
    DcMotor intake;
    DcMotor transfer;
    DcMotorEx flyWheel;
    Servo gate;
    Servo leftFlower;
    Servo rightFlower;

    public final int leftFlowerDown = 1;
    public final int leftFlowerUp = 0;
    public final int rightFlowerDown = 0;
    public final int rightFlowerUp = 1;
    public final int closeGate = 0;
    public final double openGate = 0.4;
    public static PIDFCoefficients shooterPIDF = new PIDFCoefficients(0,0,0,0);
    public static int TARGET_TICKS = 800;

    public void initOperator(HardwareMap hardwareMap) {
        gate = hardwareMap.get(Servo.class, "gate");
        leftFlower = hardwareMap.get(Servo.class, "leftFlower");
        rightFlower = hardwareMap.get(Servo.class, "rightFlower");
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");

        flyWheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        intake.setDirection(DcMotor.Direction.REVERSE);
    }
}