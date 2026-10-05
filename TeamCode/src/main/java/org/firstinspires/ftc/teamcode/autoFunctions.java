package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
//NOT DONE
public class autoFunctions {
    drivetrain robot = new drivetrain();
    constants constant = new constants();
    test2importing opmode = new test2importing();

    public boolean isDrivetrainBusy() {
        return robot.frontRightMotor.isBusy() ||
                robot.frontLeftMotor.isBusy() ||
                robot.backRightMotor.isBusy() ||
                robot.backLeftMotor.isBusy();
    }

    public void setTargetPosition(double distance) {
        double counts = constant.COUNTS_PER_INCH;
        int target = (int)(distance * counts);
        robot.frontRightMotor.setTargetPosition(target);
        robot.frontLeftMotor.setTargetPosition(target);
        robot.backRightMotor.setTargetPosition(target);
        robot.backLeftMotor.setTargetPosition(target);

    }

    public void setPower (double p) {
        robot.frontRightMotor.setPower(Math.abs(p));
        robot.frontLeftMotor.setPower(Math.abs(p));
        robot.backRightMotor.setPower(Math.abs(p));
        robot.backLeftMotor.setPower(Math.abs(p));
    }

    public void showCurrentPosition() {
        opmode.telemetry.addData("FR Position", robot.frontRightMotor.getCurrentPosition());
        opmode.telemetry.addData("FL Position", robot.frontLeftMotor.getCurrentPosition());
        opmode.telemetry.addData("BR Position", robot.backRightMotor.getCurrentPosition());
        opmode.telemetry.addData("BL Position", robot.backLeftMotor.getCurrentPosition());
    }




//    Driving Functions
    public void forward(double inches, double power) {
        robot.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setTargetPosition(inches);
        robot.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);
        while (opmode.opModeIsActive() && isDrivetrainBusy()) {
            showCurrentPosition();
            opmode.telemetry.update();
        }
        robot.setDrivePower(0,0,0,0);
        robot.setRunMode((DcMotor.RunMode.RUN_USING_ENCODER));
    }

//    public void backward(double inches, double power) {
//        robot.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        setTargetPosition(-inches);
//        robot.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
//        frontRightMotor.setPower(Math.abs(power));
//        frontLeftMotor.setPower(Math.abs(power));
//        backRightMotor.setPower(Math.abs(power));
//        backLeftMotor.setPower(Math.abs(power));
//        while (opModeIsActive() && (frontRightMotor.isBusy() || frontLeftMotor.isBusy() || backRightMotor.isBusy() || backLeftMotor.isBusy())) {
//            telemetry.addData("FR Position", frontRightMotor.getCurrentPosition());
//            telemetry.addData("FL Position", frontLeftMotor.getCurrentPosition());
//            telemetry.addData("BR Position", backRightMotor.getCurrentPosition());
//            telemetry.addData("BL Position", backLeftMotor.getCurrentPosition());
//            telemetry.update();
//        }
//        frontRightMotor.setPower(0);
//        frontLeftMotor.setPower(0);
//        backRightMotor.setPower(0);
//        backLeftMotor.setPower(0);
//        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
//

}
