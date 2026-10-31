package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;


public class shooterPIDF{
    initOperator Operator = new initOperator();
    double shootP = 0;
    double shootI = 0;
    double shootD = 0;
    double shootF = 0;
    double lastError;
    public void shooterPIDFInit (double targetVelocity) {
        PIDFCoefficients shooterPIDF = new PIDFCoefficients(shootP,shootI,shootD,shootF);
        Operator.flyWheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, shooterPIDF);

        Operator.flyWheel.setVelocity(targetVelocity);

        double curVelocity = Operator.flyWheel.getVelocity();
        double error = targetVelocity - curVelocity;
    }
}