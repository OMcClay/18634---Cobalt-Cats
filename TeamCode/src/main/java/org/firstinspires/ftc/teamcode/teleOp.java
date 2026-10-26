package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp (name = "TeleOp")
public class teleOp extends LinearOpMode {
//    DcMotor fl;
//    DcMotor fr;
//    DcMotor bl;
//    DcMotor br;
    drivetrain driveTrain = new drivetrain();
    operator Operator = new operator();
//    DcMotor intake;
//    DcMotor transfer;
//    DcMotorEx flyWheel;
//    Servo gate;
//    IMU imu;

    double intakeSpeed = 0.75;
    double driveSpeed = 1;
    boolean flyWheelStatus = false;


    @Override
    public void runOpMode() throws InterruptedException {

        driveTrain.initDrive(hardwareMap);
        Operator.initOperator(hardwareMap);
//        fl = hardwareMap.get(DcMotor.class, "frontLeft");
//        fr = hardwareMap.get(DcMotor.class, "frontRight");
//        bl = hardwareMap.get(DcMotor.class, "backLeft");
//        br = hardwareMap.get(DcMotor.class, "backRight");
//        gate = hardwareMap.get(Servo.class, "gate");
//        intake = hardwareMap.get(DcMotor.class, "intake");
//        transfer = hardwareMap.get(DcMotor.class, "transfer");
//        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");
//        br.setDirection(DcMotor.Direction.REVERSE);
//        intake.setDirection(DcMotor.Direction.REVERSE);
//        imu = hardwareMap.get(IMU.class, "imu");
//        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
//        imu.initialize(new IMU.Parameters(orientationOnRobot));

        waitForStart();
        while (opModeIsActive()) {
            fieldCentricDrive();
            operator();
        }
    }

    public void botCentricDrive(){
        driveTrain.frontLeftMotor.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
        driveTrain.frontRightMotor.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);
        driveTrain.backLeftMotor.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);
        driveTrain.backRightMotor.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);
    }

    public void fieldCentricDrive() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double pivot = gamepad1.right_stick_x;

        if (gamepad1.start) {
            driveTrain.imu.resetYaw();
        }

        double heading = driveTrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        if (gamepad1.right_bumper) {
            if (driveSpeed == 1) {
                driveSpeed = 0.45;
            }
            else {
                driveSpeed = 1;
            }
        }

        double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
        double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);
        rotX *= 1.1;
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(pivot), 1.0);
        double frontLeftPower = -(rotY + rotX + pivot) / denominator;
        double backLeftPower = -(rotY - rotX + pivot) / denominator;
        double frontRightPower = -(rotY - rotX - pivot) / denominator;
        double backRightPower = -(rotY + rotX - pivot) / denominator;
        driveTrain.frontLeftMotor.setPower(frontLeftPower * driveSpeed);
        driveTrain.backLeftMotor.setPower(backLeftPower * driveSpeed);
        driveTrain.frontRightMotor.setPower(frontRightPower * driveSpeed);
        driveTrain.backRightMotor.setPower(backRightPower * driveSpeed);
        telemetry.update();
    }

    public void operator() {
        final int closeGate = 0;
        final double openGate = 0.3;
        String gatePos = "Closed";
        ElapsedTime gateTime = new ElapsedTime();

        if (gamepad2.left_stick_y != 0){
            intakeSpeed = (0.75 * (-gamepad2.left_stick_y));
        }
        else {
            intakeSpeed = (0.75);
        }


        if (gamepad2.triangle && !flyWheelStatus) {
            Operator.flyWheel.setPower(0.75);
            flyWheelStatus = true;
            gateTime.reset();
        } else if (gamepad2.triangle && flyWheelStatus) {
            Operator.flyWheel.setPower(0);
            flyWheelStatus = false;
            gateTime.reset();
        }

//        if (gamepad2.rightBumperWasPressed() && gate.getPosition() == closeGate) {
//            gate.setPosition(openGate);
//        }
//        else if (gamepad2.rightBumperWasPressed() && gate.getPosition() == openGate) {
//            gate.setPosition(closeGate);
//        }

        Operator.intake.setPower(gamepad2.left_trigger * intakeSpeed);
        Operator.transfer.setPower(gamepad2.left_trigger * intakeSpeed);
        if (gateTime.seconds() > 0.5) {
            if (gamepad2.right_bumper && Operator.gate.getPosition() == closeGate) {
                Operator.gate.setPosition(openGate);
                gatePos = "closed";
           }
            else if (gamepad2.right_bumper && gatePos == "closed") {
                Operator.gate.setPosition(closeGate);
                gatePos = "open";
        }
//        for (int i = 0; i<=1; i += 0.1) {
//            gate.setPosition(i);
//            if (i >= 1) {
//                i = 0;
//            }
//            sleep(500);
//        }

        telemetry.addData("gatePos", Operator.gate.getPosition());
        telemetry.addData("velocity", Operator.flyWheel.getVelocity());
        telemetry.addData("flyWheel", flyWheelStatus);
        telemetry.update();
    }
}