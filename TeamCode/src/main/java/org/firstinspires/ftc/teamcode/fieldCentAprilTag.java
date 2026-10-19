
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@TeleOp(name = "fieldCentricAttempt1")
public class fieldCentAprilTag extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private IMU imu;

    private Servo cameraServo;
    private AprilTagProcessor tagProcessor;
    private VisionPortal visionPortal;

    private double cameraPosition = 0.5;

    @Override
    public void runOpMode() {
        cameraServo = hardwareMap.get(Servo.class, "gate");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        cameraServo.setPosition(cameraPosition);
        tagProcessor = new AprilTagProcessor.Builder().build();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Eyes"))
                .addProcessor(tagProcessor)
                .build();
        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;
            if (gamepad1.start) imu.resetYaw();
            double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            telemetry.addData("Heading(in degrees): ", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
            double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);
            rotX *= 1.1;
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1.0);
            double frontLeftPower = -(rotY + rotX + rx) / denominator;
            double backLeftPower = -(rotY - rotX + rx) / denominator;
            double frontRightPower = -(rotY - rotX - rx) / denominator;
            double backRightPower = -(rotY + rotX - rx) / denominator;
            boolean clusterFound = false;
            for (AprilTagDetection detection : tagProcessor.getDetections()) {
                if (detection instanceof AprilTagClusterDetection) {
                    AprilTagClusterDetection cluster = (AprilTagClusterDetection) detection;
                    telemetry.addData("Distance to cluster", cluster.ftcPose.range);
                    break;
                }
            }
            if (!clusterFound) telemetry.addData("Cluster", "Searching");
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);
            sleep(5);
            telemetry.update();
        }
        visionPortal.close();
    }
}