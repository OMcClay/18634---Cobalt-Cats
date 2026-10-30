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

        intake.setDirection(DcMotor.Direction.REVERSE);
    }
}



//package org.firstinspires.ftc.teamcode;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.util.ElapsedTime;

//@Autonomous(name = "dashboard")
//@Config
//public class timer extends LinearOpMode {
//    ElapsedTime timer = new ElapsedTime();
//
//    public static double reference = 500;
//    double state;
//    public static double kP = 0;
//    public static double kI = 0;
//    public static double kD = 0;
//    public static double kF = 0;
//    public static double integralSum = 0;
//    double lastError = 0;
//    public static double time = 1000;
//    FtcDashboard dashboard;
//    DcMotor testMotor;
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        dashboard = FtcDashboard.getInstance();
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//        testMotor = hardwareMap.get(DcMotor.class, "topShooter");
//        waitForStart();
//        while (opModeIsActive()){
//            if (timer.milliseconds()>5000){
//                timer.reset();
//            }
//            telemetry.addData("Test", time);
//            telemetry.update();
//            testMotor.setPower(PIDControls(reference, testMotor.getCurrentPosition()));
//            telemetry.addData("Current position", testMotor.getCurrentPosition());
//        }
//    }
//
//    double PIDControls(double reference, double state) {
//        double error = reference - state;
//        integralSum += error * timer.seconds();
//        double derivative = (error-lastError)/timer.seconds();
//        lastError = error;
//        timer.reset();
//        return error * kP + integralSum + kI + derivative * kD + reference * kF;
//
//    }
//}