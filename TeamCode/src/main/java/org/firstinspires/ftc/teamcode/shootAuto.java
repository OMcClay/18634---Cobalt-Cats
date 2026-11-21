package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@Autonomous(name = "shoot_auto", group = "Autonomous", preselectTeleOp = "TeleOp")
public class shootAuto extends LinearOpMode {
    robot robot = new robot(); // Instantiates your structural files
    autoFunctions autoFunc;

    AprilTagProcessor tagProcessor;
    VisionPortal visionPortal;


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        autoFunc = new autoFunctions(this, robot);
        double set_power = 0.5;

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
            autoFunc.left(36.0, set_power);
            autoFunc.forward(68.0, set_power);
            autoFunc.left(25.0, set_power);
        
//            autoFunc.left(36.0, set_power);
//            autoFunc.forward(26.0, set_power);
//            autoFunc.turn_right(90.0, set_power);
//            autoFunc.backward(20.0, set_power);
//            //INTAKE from flower
//            autoFunc.forward(20.0, set_power);
//            autoFunc.left(74.0, set_power);
//            autoFunc.forward(36.0, set_power);
//            autoFunc.turn_right(90.0, set_power);
//            //SHOOT
//            autoFunc.right(12.0, set_power);
//            autoFunc.backward(18.0, set_power);
//            autoFunc.forward(18.0, set_power);
//            autoFunc.left(12.0, set_power);
//            //SHOOT
//            autoFunc.right(60.0, set_power);
//            autoFunc.forward(12.0, set_power);

        }
    }
}