package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@Autonomous(name = "new_auto", group = "Autonomous", preselectTeleOp = "TeleOp")
public class newAuto extends LinearOpMode {
    robot robot = new robot(); // Instantiates your structural files
    autoFunctions autoFunc;

    AprilTagProcessor tagProcessor;
    VisionPortal visionPortal;


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        autoFunc = new autoFunctions(this, robot);
        double set_power = 0.75;
        ElapsedTime timer = new ElapsedTime();

        tagProcessor = new AprilTagProcessor.Builder().build();
        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "Eyes")).addProcessor(tagProcessor).build();
        double closestCluster = Double.MAX_VALUE;
        boolean clusterFound = false;
        String closestClusterName = "Lorem Ipsum :)"; //Clears errors

        telemetry.addData("Initialize", "Initialized");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            for (AprilTagDetection detection : tagProcessor.getDetections()) {
                if (detection instanceof AprilTagClusterDetection) {
                    AprilTagClusterDetection detectedCluster = (AprilTagClusterDetection) detection;
                    String clusterName = detectedCluster.metadata.name;
                    double clusterRoll = detectedCluster.ftcPose.roll;

                    //right side up?
                    if (Math.abs(clusterRoll) < 90) {
                        double tagDist = detectedCluster.ftcPose.range;
                        if (tagDist < closestCluster) {
                            closestClusterName = clusterName;
                            closestCluster = tagDist;

                            clusterFound = true;
                        }
                    }
                }
            }

            if (clusterFound) {
                if (closestClusterName.startsWith("R")) {
                    blackboard.put("Alliance", "Red");
                } else if (closestClusterName.startsWith("B")) {
                    blackboard.put("Alliance", "Blue");
                } else {
                    //Shouldn't actually be possible, but to clear errors and just in cases
                    blackboard.put("Alliance", "None");
                }
            } else {
                blackboard.put("Alliance", "None");
            }
            telemetry.update();

            robot.operator.gate.setPosition(robot.operator.openGate);
            autoFunc.forward(4.0, set_power);
            autoFunc.shoot(1200);
            autoFunc.turn_right(115, set_power); //actually 90
            autoFunc.right(7, set_power);
            robot.operator.intake.setPower(0.5);
            robot.operator.transfer.setPower(0.5);
            autoFunc.backward(60.0, set_power);
            autoFunc.forward(10, set_power);
            autoFunc.backward(15, set_power);
            robot.operator.intake.setPower(0);
            robot.operator.transfer.setPower(0);
            autoFunc.forward(60.0, set_power);
            autoFunc.left(5, set_power);
            autoFunc.turn_left(115, set_power); //actually 90
            sleep(250);
            timer.reset();
            // should be more fixed, not tested yet
            boolean matchesAlliance = false;
            String savedAlliance = String.valueOf(blackboard.get("Alliance"));
            while (opModeIsActive() && timer.seconds() < 3) {
                for (AprilTagDetection detection : tagProcessor.getDetections()) {
                    if (detection instanceof AprilTagClusterDetection) {
                        AprilTagClusterDetection currentCluster = (AprilTagClusterDetection) detection;
                        if (currentCluster.metadata != null && currentCluster.metadata.name.startsWith(String.valueOf(savedAlliance.charAt(0)))) {
                            matchesAlliance = true;
                            break;
                        }
                    }
                }
                telemetry.addData("Matches Alliance: ", matchesAlliance);
                telemetry.addData("Saved Alliance: ", savedAlliance);
                telemetry.update();
                if (matchesAlliance) {
                    break;
                }
            }
            if (matchesAlliance) {
                autoFunc.shoot(1200);
                autoFunc.forward(68.0, set_power);
                autoFunc.turn_right(195.0, set_power); //actually 180
                autoFunc.right(46.0, set_power);
            } else {
                autoFunc.forward(68.0, set_power);
                autoFunc.turn_right(195.0, set_power); //actually 180
                autoFunc.shoot(1200);
                autoFunc.right(46.0, set_power);
            }

        }
    }
}