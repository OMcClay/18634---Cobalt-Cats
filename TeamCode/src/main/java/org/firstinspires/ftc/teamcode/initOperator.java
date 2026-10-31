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

    public final double leftFlowerDown = 0.95;
    public final double leftFlowerUp = 0;
    public final double rightFlowerDown = 0.05;
    public final double rightFlowerUp = 1;
    public final double closeGate = 0;
    public final double openGate = 0.4;
    double shootP = 90;
    double shootI = 0;
    double shootD = 9;
    double shootF = 14.5;
    public PIDFCoefficients shooterPIDF = new PIDFCoefficients(shootP,shootI,shootD,shootF);

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