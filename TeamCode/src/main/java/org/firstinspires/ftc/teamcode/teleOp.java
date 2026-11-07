package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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

@TeleOp (name = "TeleOp")
public class teleOp extends LinearOpMode {
    drivetrain driveTrain = new drivetrain();
    initOperator Operator = new initOperator();

    double intakeSpeed = 0.75;
    double driveSpeed = 1;
    boolean flyWheelStatus = false;
    ElapsedTime timer = new ElapsedTime();
    double flyWheelTime;
    double lastShootTime;
    double targetVelocity;
    double tagDist;
    String gatePos = "closed";
    String alliance;
    YawPitchRollAngles robotOrientation;
    AprilTagProcessor tagProcessor;
    VisionPortal visionPortal;
//    private IMU imu;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        tagProcessor = new AprilTagProcessor.Builder().build();
        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "Eyes")).addProcessor(tagProcessor).build();
        driveTrain.initDrive(hardwareMap);
        Operator.initOperator(hardwareMap);
        alliance = (String) blackboard.get("Alliance");

        waitForStart();
        timer.reset();
        while (opModeIsActive()) {
            robotOrientation = driveTrain.imu.getRobotYawPitchRollAngles();
            driver();
            operator();

        }
    }

    public void driver() {
//         botCentricDrive();
        fieldCentricDrive();
        aprilTag();

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

        // State what alliance robot is
        if (gamepad1.dpad_up) {
            alliance = "Blue";
        } else if (gamepad1.dpad_down) {
            alliance = "Red";
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
        //Intake & transfer speed
        if ((gamepad2.left_stick_y >= 0.1) || (gamepad2.left_stick_y <= -0.1)){
            intakeSpeed = (0.8 * (-gamepad2.left_stick_y));
        }
        else {
            intakeSpeed = (0.8);
        }

        //Gate Control
        if (targetVelocity != 0) {
            if ((Operator.flyWheel.getVelocity() > (targetVelocity - 100) && Operator.flyWheel.getVelocity() < (targetVelocity + 100) && !(gamepad2.right_bumper)) || (gamepad2.right_bumper && gatePos == "closed")) {
                gatePos = "open";
                Operator.gate.setPosition(Operator.openGate);
            } else if (!(Operator.flyWheel.getVelocity() > (targetVelocity - 100) && Operator.flyWheel.getVelocity() < (targetVelocity + 100) && !(gamepad2.right_bumper)) || (gamepad2.right_bumper && gatePos == "open")) {
                gatePos = "closed";
                Operator.gate.setPosition(Operator.closeGate);
            }
        }


        // Shoot
        if (gamepad2.square) {
            //Target Velocity from equation
            if (!(tagDist >= 0)) {
                targetVelocity = 1300;
            } else {
                targetVelocity = Operator.velocityM * tagDist + Operator.velocityB;
            }

            //double targetVelocity = 1300;

            if (lastShootTime > timer.seconds() - 1.25) {
                Operator.transfer.setPower(-0.3);
                Operator.intake.setPower(-0.15);
                Operator.flyWheel.setVelocity(-1000);
            } else {
                Operator.flyWheel.setVelocity(1300);
            }

            if (Operator.gate.getPosition() == Operator.openGate && lastShootTime < timer.seconds() + 1.3) {
                if (!(gamepad2.left_trigger > 0.2)) {
                    Operator.transfer.setPower(0.85);
                    Operator.intake.setPower(0.85);
                } else {
                    Operator.transfer.setPower(gamepad2.left_trigger * intakeSpeed);
                    Operator.intake.setPower(gamepad2.left_trigger * intakeSpeed);
                }

            }
        } else {
            if (gamepad2.squareWasReleased()) {
                Operator.flyWheel.setVelocity(0);
            }
            if (flyWheelTime <= timer.seconds() - 0.3) {
                if (gamepad2.triangle && !flyWheelStatus) {
                    // toggle on
                    targetVelocity = 1300;
                    flyWheelStatus = true;
                    flyWheelTime = timer.seconds();
                } else if (gamepad2.triangle && flyWheelStatus) {
                    // toggle off
                    targetVelocity = 0;
                    flyWheelStatus = false;
                    flyWheelTime = timer.seconds();
                } else if (gamepad2.touchpad) {
                    //poop
                    targetVelocity = 900;
                    flyWheelTime = timer.seconds();
                } else if (gamepad2.share) {
                    // reverse
                    targetVelocity = -2100;
                    flyWheelTime = timer.seconds();
                } else if (gamepad2.b) {
                    //stop
                    targetVelocity = 0;
                    flyWheelTime = timer.seconds();
                }
                Operator.flyWheel.setVelocity(targetVelocity);
            }
            Operator.intake.setPower(gamepad2.left_trigger * intakeSpeed);
            Operator.transfer.setPower(gamepad2.left_trigger * intakeSpeed);
            lastShootTime = timer.seconds();
        }

        telemetry.addData("velocity", Operator.flyWheel.getVelocity());
        telemetry.addData("imu", robotOrientation.getYaw(AngleUnit.DEGREES));
        telemetry.update();
    }

    public void aprilTag() {
        boolean clusterFound = false;
        for (AprilTagDetection detection : tagProcessor.getDetections()) {
            if (detection instanceof AprilTagClusterDetection) {
                AprilTagClusterDetection detectedCluster = (AprilTagClusterDetection) detection;
                String clusterName = detectedCluster.metadata.name;
                double clusterRoll = detectedCluster.ftcPose.roll;

                //Correct alliance?
                if (clusterName.startsWith("R") && alliance == "Red" || clusterName.startsWith("B") && alliance == "Blue") {
                    //Is it down or up?
                    if (Math.abs(clusterRoll) < 90) {
                        tagDist = detectedCluster.ftcPose.range;
                        telemetry.addData("Distance to cluster", tagDist);
                        telemetry.addData("Cluster Info", clusterName);
                        telemetry.update();
                        break;
                    }
                }
            }
        }

        if (!clusterFound) {
            telemetry.addData("Cluster Tag", "Searching");
        }
        telemetry.update();
    }
}