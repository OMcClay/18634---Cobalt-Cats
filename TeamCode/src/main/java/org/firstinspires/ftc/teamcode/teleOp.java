package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import com.qualcomm.robotcore.hardware.IMU;

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
    YawPitchRollAngles robotOrientation;
    AprilTagProcessor tagProcessor;
    VisionPortal visionPortal;
    private IMU imu;

    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.FORWARD;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.LEFT;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        tagProcessor = new AprilTagProcessor.Builder().build();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Eyes"))
                .addProcessor(tagProcessor)
                .build();
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
            boolean clusterFound = false;
            for (AprilTagDetection detection : tagProcessor.getDetections()) {
                if (detection instanceof AprilTagClusterDetection) {
                    AprilTagClusterDetection cluster = (AprilTagClusterDetection) detection;
                    telemetry.addData("Distance to cluster", cluster.ftcPose.range);
                    break;
                }
            }
            if (!clusterFound) telemetry.addData("Cluster", "Searching");
            telemetry.update();
        }
    }

    public void driver() {
//         botCentricDrive();
        fieldCentricDriveO();

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
        if (gamepad1.leftBumperWasPressed()) {
            if (flowerStatus == "down") {
                Operator.flower.setPosition(Operator.flowerUp);
                flowerStatus = "up";
            } else if (flowerStatus == "up") {
                Operator.flower.setPosition(Operator.flowerDown);
                flowerStatus = "down";
            }
        }

    }
    public void botCentricDrive(){
        driveTrain.frontLeftMotor.setPower((gamepad1.left_stick_y + gamepad1.left_stick_y) * driveSpeed);
        driveTrain.frontRightMotor.setPower((gamepad1.left_stick_y - gamepad1.left_stick_y) * driveSpeed);
        driveTrain.backLeftMotor.setPower((gamepad1.left_stick_y - gamepad1.left_stick_y) * driveSpeed);
        driveTrain.backRightMotor.setPower((gamepad1.left_stick_y - gamepad1.left_stick_y) * driveSpeed);
    }
    public void fieldCentricDriveO() {
        if (gamepad1.start) driveTrain.imu.resetYaw();
        double y = gamepad1.left_stick_y;
        double x = -gamepad1.left_stick_x;
        double pivot = -gamepad1.right_stick_x;

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

        telemetry.addData("Yaw", yaw);
        telemetry.addData("Drive Speed", driveSpeed);
        telemetry.update();
    }

    public void operator() {
        double targetVelocity = 1300;
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


        //Fly Wheel on/off
        if (flyWheelTime <= timer.seconds() - 0.3) {
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
            }
        }

        if (Operator.flyWheel.getVelocity() > (targetVelocity - 100) && Operator.flyWheel.getVelocity() < (targetVelocity + 100)) {
            Operator.gate.setPosition(Operator.openGate);
            Operator.transfer.setPower(gamepad2.left_trigger * intakeSpeed);
        } else {
            Operator.transfer.setPower(-0.1);
            Operator.gate.setPosition(Operator.closeGate);
            if (gateTime <= timer.seconds() - 1) {
                if (gamepad2.right_bumper && gatePos == "open") {
                    Operator.gate.setPosition(Operator.openGate);
                    gatePos = "closed";
                    gateTime = timer.seconds();
                }
                else if (gamepad2.right_trigger>1 && gatePos == "closed") {
                    Operator.gate.setPosition(Operator.closeGate);
                    gatePos = "open";
                    gateTime = timer.seconds();
                } else {
                    Operator.gate.setPosition(Operator.closeGate);
                }
            }
        }

//        if (gateTime <= timer.seconds() - 1) {
//            if (gamepad2.right_bumper && gatePos == "open") {
//                Operator.gate.setPosition(Operator.openGate);
//                gatePos = "closed";
//                gateTime = timer.seconds();
//            }
//            else if (gamepad2.right_bumper && gatePos == "closed") {
//                Operator.gate.setPosition(Operator.closeGate);
//                gatePos = "open";
//                gateTime = timer.seconds();
//            }
//        }

        telemetry.addData("gatePos", Operator.gate.getPosition());
        telemetry.addData("velocity", Operator.flyWheel.getVelocity());
        telemetry.addData("flyWheel", flyWheelStatus);
        telemetry.addData("imu", robotOrientation.getYaw(AngleUnit.DEGREES));
        telemetry.update();
    }

}