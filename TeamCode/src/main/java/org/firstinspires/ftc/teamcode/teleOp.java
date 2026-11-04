package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp (name = "TeleOp")
public class teleOp extends LinearOpMode {
    drivetrain driveTrain = new drivetrain();
    initOperator Operator = new initOperator();
    aprilTag AprilTag = new aprilTag();

    double intakeSpeed = 0.75;
    double driveSpeed = 1;
    boolean flyWheelStatus = false;
    ElapsedTime timer = new ElapsedTime();
    double flyWheelTime;
    double lastShootTime;
    String lastShoot = "none";
    String gatePos = "closed";
    YawPitchRollAngles robotOrientation;

    private IMU imu;

    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.FORWARD;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.LEFT;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        imu.resetYaw();
        driveTrain.initDrive(hardwareMap);
        Operator.initOperator(hardwareMap);
        waitForStart();
        timer.reset();
        while (opModeIsActive()) {
            robotOrientation = imu.getRobotYawPitchRollAngles();
            driver();
            operator();
            telemetry.update();
        }
    }

    public void driver() {
//         botCentricDrive();
        fieldCentricDrive();

        // Drive Speed
        if (gamepad1.dpadUpWasPressed()) {
            if (driveSpeed == 1) {
                driveSpeed = 0.45;
            }
            else {
                driveSpeed = 1;
            }
        }

        //Flower servos
        if (gamepad1.left_bumper) {
            Operator.flower.setPower(1);
        } else if (gamepad1.right_bumper) {
            Operator.flower.setPower(-1);
        } else {
            Operator.flower.setPower(0);
        }

    }
    public void botCentricDrive(){
        driveTrain.frontLeftMotor.setPower((gamepad1.left_stick_y + gamepad1.left_stick_y) * driveSpeed);
        driveTrain.frontRightMotor.setPower((gamepad1.left_stick_y - gamepad1.left_stick_y) * driveSpeed);
        driveTrain.backLeftMotor.setPower((gamepad1.left_stick_y - gamepad1.left_stick_y) * driveSpeed);
        driveTrain.backRightMotor.setPower((gamepad1.left_stick_y - gamepad1.left_stick_y) * driveSpeed);
    }
    public void fieldCentricDrive() {
        if (gamepad1.start) driveTrain.imu.resetYaw();
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double pivot = gamepad1.right_stick_x;

        double yaw = robotOrientation.getYaw(AngleUnit.RADIANS);

        double rotX = x * Math.cos(-yaw) - y * Math.sin(-yaw);
        double rotY = x * Math.sin(-yaw) + y * Math.cos(-yaw);

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(pivot), 1);

        double frontLeft = rotY + rotX + pivot;
        double frontRight = rotY - rotX - pivot;
        double backLeft = rotY - rotX + pivot;
        double backRight = rotY + rotX - pivot;

        driveTrain.frontLeftMotor.setPower(frontLeft / denominator * driveSpeed);
        driveTrain.frontRightMotor.setPower(frontRight / denominator * driveSpeed);
        driveTrain.backLeftMotor.setPower(backLeft / denominator * driveSpeed);
        driveTrain.backRightMotor.setPower(backRight / denominator * driveSpeed);
    }

    public void operator() {
        double targetVelocity = 1300;
        //Intake & transfer speed
        if ((gamepad2.left_stick_y >= 0.1) || (gamepad2.left_stick_y <= -0.1)){
            intakeSpeed = (0.75 * (-gamepad2.left_stick_y));
        }
        else {
            intakeSpeed = (0.75);
        }


        //Gate Control
        if (gamepad2.right_bumper) {
            if (gamepad2.right_bumper && gatePos == "closed") {
                gatePos = "open";
                Operator.gate.setPosition(Operator.openGate);
            }
        } else {
            if (Operator.flyWheel.getVelocity() > (targetVelocity - 100) && Operator.flyWheel.getVelocity() < (targetVelocity + 100)) {
                gatePos = "open";
                Operator.gate.setPosition(Operator.openGate);
            } else if (!(Operator.flyWheel.getVelocity() > (targetVelocity - 100) && Operator.flyWheel.getVelocity() < (targetVelocity + 100))) {
                gatePos = "closed";
                Operator.gate.setPosition(Operator.closeGate);
            }
        }


        // Shoot
        if (gamepad2.square) {
            lastShoot = "square";
            if (lastShootTime > timer.seconds() - 0.75) {
                Operator.flyWheel.setVelocity(-1000);
                Operator.transfer.setPower(-0.25);
                Operator.intake.setPower(-0.1);
            } else {
                Operator.flyWheel.setVelocity(1300);
            }

            if (Operator.gate.getPosition() == Operator.openGate && lastShootTime < timer.seconds() + 0.75) {
                if (!(gamepad2.left_trigger > 0.2)) {
                    Operator.transfer.setPower(0.75);
                    Operator.intake.setPower(0.75);
                } else {
                    Operator.transfer.setPower(gamepad2.left_trigger * intakeSpeed);
                    Operator.intake.setPower(gamepad2.left_trigger * intakeSpeed);
                }

            }
        } else {
            if (lastShoot == "square") {
                Operator.flyWheel.setVelocity(0);
                lastShoot = "no";
            } else if (flyWheelTime <= timer.seconds() - 0.3) {
                if (gamepad2.triangle && !flyWheelStatus) {
                    Operator.flyWheel.setVelocity(1300);
                    flyWheelStatus = true;
                    flyWheelTime = timer.seconds();
                } else if (gamepad2.triangle && flyWheelStatus) {
                    Operator.flyWheel.setVelocity(0);
                    flyWheelStatus = false;
                    flyWheelTime = timer.seconds();
                } else if (gamepad2.touchpad) {
                    Operator.flyWheel.setVelocity(900);
                } else if (gamepad2.share) {
                    Operator.flyWheel.setVelocity(-2100);
                } else if (gamepad2.b) {
                    Operator.flyWheel.setVelocity(0);
                }
            }
            Operator.intake.setPower(gamepad2.left_trigger * intakeSpeed);
            Operator.transfer.setPower(gamepad2.left_trigger * intakeSpeed);
            lastShootTime = timer.seconds();
        }

        telemetry.addData("velocity", Operator.flyWheel.getVelocity());
        telemetry.addData("imu", robotOrientation.getYaw(AngleUnit.DEGREES));
        telemetry.update();
    }

}


