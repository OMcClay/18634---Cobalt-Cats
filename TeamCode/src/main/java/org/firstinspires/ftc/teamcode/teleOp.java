package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp (name = "TeleOp")
public class teleOp extends LinearOpMode {
    drivetrain driveTrain = new drivetrain();
    initOperator Operator = new initOperator();

    double intakeSpeed = 0.75;
    double driveSpeed = 1;
    String flowerStatus = "down";
    boolean flyWheelStatus = false;
    ElapsedTime timer = new ElapsedTime();
    double flyWheelTime;
    double gateTime;
    String gatePos = "closed";


    @Override
    public void runOpMode() throws InterruptedException {

        driveTrain.initDrive(hardwareMap);
        Operator.initOperator(hardwareMap);


        waitForStart();
        timer.reset();
        while (opModeIsActive()) {
            driver();
            operator();
            telemetry.update();
        }
    }

    public void driver() {
        // botCentricDrive();
        fieldCentricDrive();

        // Drive Speed
        if (gamepad1.right_bumper) {
            if (driveSpeed == 1) {
                driveSpeed = 0.45;
            }
            else {
                driveSpeed = 1;
            }
        }

        //Flower servos down/up
        if (gamepad1.left_bumper) {
            if (flowerStatus == "down") {
                Operator.leftFlower.setPosition(Operator.leftFlowerUp);
                Operator.rightFlower.setPosition(Operator.rightFlowerUp);
                flowerStatus = "up";
            } else if (flowerStatus == "up") {
                Operator.leftFlower.setPosition(Operator.leftFlowerDown);
                Operator.rightFlower.setPosition(Operator.rightFlowerDown);
                flowerStatus = "down";
            }
        }

    }
    public void botCentricDrive(){
        driveTrain.frontLeftMotor.setPower((gamepad1.left_stick_y + gamepad1.left_stick_x) * driveSpeed);
        driveTrain.frontRightMotor.setPower((gamepad1.left_stick_y - gamepad1.left_stick_x) * driveSpeed);
        driveTrain.backLeftMotor.setPower((gamepad1.left_stick_y - gamepad1.left_stick_x) * driveSpeed);
        driveTrain.backRightMotor.setPower((gamepad1.left_stick_y - gamepad1.left_stick_x) * driveSpeed);
    }

    public void fieldCentricDrive() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double pivot = gamepad1.right_stick_x;

        //imu
        if (gamepad1.start) {
            driveTrain.imu.resetYaw();
        }
        double heading = driveTrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        //fc equations
        double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
        double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);
        rotX *= 1.1;
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(pivot), 1.0);
        double frontLeftPower = -(rotY + rotX + pivot) / denominator;
        double backLeftPower = -(rotY - rotX + pivot) / denominator;
        double frontRightPower = -(rotY - rotX - pivot) / denominator;
        double backRightPower = -(rotY + rotX - pivot) / denominator;

        //
        driveTrain.frontLeftMotor.setPower(frontLeftPower * driveSpeed);
        driveTrain.backLeftMotor.setPower(backLeftPower * driveSpeed);
        driveTrain.frontRightMotor.setPower(frontRightPower * driveSpeed);
        driveTrain.backRightMotor.setPower(backRightPower * driveSpeed);
    }

    public void operator() {
        //Intake & transfer speed
        if (!(gamepad2.left_stick_y > 0.1) || !(gamepad2.left_stick_y < 0.1)){
            intakeSpeed = (0.75 * (-gamepad2.left_stick_y));
        }
        else {
            intakeSpeed = (0.75);
        }

        Operator.intake.setPower(gamepad2.left_trigger * intakeSpeed);
        Operator.transfer.setPower(gamepad2.left_trigger * intakeSpeed);

        // Gate open/close
        if (gateTime >= timer.seconds() - 0.3) {
            if (gamepad2.right_bumper && gatePos == "open") {
                Operator.gate.setPosition(Operator.openGate);
                gatePos = "closed";
                gateTime = timer.seconds();
            }
            else if (gamepad2.right_bumper && gatePos == "closed") {
                Operator.gate.setPosition(Operator.closeGate);
                gatePos = "open";
                gateTime = timer.seconds();
            }
        }

        //Fly Wheel on/off
        if (flyWheelTime <= timer.seconds() - 0.3) {
            if (gamepad2.triangle && !flyWheelStatus) {
                Operator.flyWheel.setPower(0.75);
                flyWheelStatus = true;
                flyWheelTime = timer.seconds();
            } else if (gamepad2.triangle && flyWheelStatus) {
                Operator.flyWheel.setPower(0);
                flyWheelStatus = false;
                flyWheelTime = timer.seconds();
            }
        }

        telemetry.addData("gatePos", Operator.gate.getPosition());
        telemetry.addData("velocity", Operator.flyWheel.getVelocity());
        telemetry.addData("flyWheel", flyWheelStatus);
        telemetry.update();
    }

    public void shooterSpeed() {
        MultipleTelemetry tel = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        ElapsedTime shooterTime = new ElapsedTime();
        double lastError = 0;
        double integralSum = 0;
        double shooterTimeSeconds = shooterTime.seconds();
        shooterTime.reset();

        int currentPos = Operator.flyWheel.getCurrentPosition();
        double error = Operator.TARGET_TICKS - currentPos;

        integralSum = Math.max(-1, Math.min(1, integralSum + error * shooterTimeSeconds));
        double derivative = (error - lastError) / shooterTimeSeconds;
        lastError = error;

        double power = Operator.shooterPIDF.p * error + Operator.shooterPIDF.i * integralSum + Operator.shooterPIDF.d * derivative + Operator.shooterPIDF.f;

        Operator.flyWheel.setPower(Math.max(-1, Math.min(1, power)));

        tel.addData("Target", Operator.TARGET_TICKS);
        tel.addData("Position", currentPos);
        tel.addData("Error", error);
        tel.addData("Power", power);
        tel.update();
    }
}