//package org.firstinspires.ftc.teamcode;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.PIDFCoefficients;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//
//public class shooterPIDF extends LinearOpMode {
//    public static PIDFCoefficients shooterPIDF = new PIDFCoefficients(0,0,0,0);
//    public static int TARGET_TICKS = 800;
//    ElapsedTime timer = new ElapsedTime();
//    double lastError = 0;
//    double integralSum = 0;
//    FtcDashboard dashboard;
//    public void shooterPIDFInit () {
//        dashboard = FtcDashboard.getInstance();
//        MultipleTelemetry tel = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//
//        telemetry.addData();
//        double dt = timer.seconds();
//        timer.reset();
//
//        int currentPos = lift.getCurrentPosition();
//        double error = LiftConstants.TARGET_TICKS - currentPos;
//
//        integralSum = Math.max(-1, Math.min(1, integralSum + error * dt));
//        double derivative = (error - lastError) / dt;
//        lastError = error;
//
//        PIDFCoefficients c = LiftConstants.PIDF;
//        double power = c.p * error + c.i * integralSum + c.d * derivative + c.f;
//
//        lift.setPower(Math.max(-1, Math.min(1, power)));
//
//        tel.addData("Target", LiftConstants.TARGET_TICKS);
//        tel.addData("Position", currentPos);
//        tel.addData("Error", error);
//        tel.addData("Power", power);
//        tel.update();
//    }
//}