package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class initOperator {
    DcMotor intake;
    DcMotor transfer;
    DcMotorEx flyWheel;
    Servo gate;
//    Servo flower;
    CRServo flower;
    DigitalChannel leftLEDG;
    DigitalChannel leftLEDR;
    DigitalChannel rightLEDG;
    DigitalChannel rightLEDR;
    RevBlinkinLedDriver stripLED;
//    public final double flowerDown = 0.95;
//    public final double flowerUp = 0;
    public final double closeGate = 0;
    public final double openGate = 0.4;
    public static double shootP = 90;
    public static double shootI = 0.075;
    public static double shootD = 9;
    public static double shootF = 14.5;
    double velocityM = 4;
    double velocityB = 942;
    public PIDFCoefficients shooterPIDF = new PIDFCoefficients(shootP,shootI,shootD,shootF);

    public void initOperator(HardwareMap hardwareMap) {
        gate = hardwareMap.get(Servo.class, "gate");
        flower = hardwareMap.get(CRServo.class, "flower");
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");
        leftLEDG = hardwareMap.get(DigitalChannel.class,"leftLEDG");
        leftLEDR = hardwareMap.get(DigitalChannel.class,"leftLEDR");
        rightLEDR = hardwareMap.get(DigitalChannel.class,"rightLEDR");
        rightLEDG = hardwareMap.get(DigitalChannel.class,"rightLEDG");
        stripLED = hardwareMap.get(RevBlinkinLedDriver.class, "stripLED");

        leftLEDG.setMode(DigitalChannel.Mode.OUTPUT);
        leftLEDR.setMode(DigitalChannel.Mode.OUTPUT);
        rightLEDG.setMode(DigitalChannel.Mode.OUTPUT);
        rightLEDR.setMode(DigitalChannel.Mode.OUTPUT);

        flyWheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, shooterPIDF);
        flyWheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        intake.setDirection(DcMotor.Direction.REVERSE);
    }
}