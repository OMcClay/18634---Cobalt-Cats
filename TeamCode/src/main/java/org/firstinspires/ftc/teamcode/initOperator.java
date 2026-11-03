package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
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
//    Servo flower;
    CRServo flower;
//    public final double flowerDown = 0.95;
//    public final double flowerUp = 0;
    public final double closeGate = 0;
    public final double openGate = 0.4;
    double shootP = 90;
    double shootI = 0;
    double shootD = 9;
    double shootF = 14.5;
    public PIDFCoefficients shooterPIDF = new PIDFCoefficients(shootP,shootI,shootD,shootF);

    public void initOperator(HardwareMap hardwareMap) {
        gate = hardwareMap.get(Servo.class, "gate");
        flower = hardwareMap.get(CRServo.class, "flower");
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");

        flyWheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, shooterPIDF);
        flyWheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        intake.setDirection(DcMotor.Direction.REVERSE);
    }
}