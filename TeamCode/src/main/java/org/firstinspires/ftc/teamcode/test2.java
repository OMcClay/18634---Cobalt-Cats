
package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "camera2")
public class test2 extends LinearOpMode {
    private Servo cameraServo;
    private AprilTagProcessor tagProcessor;
    private VisionPortal visionPortal;

    private double cameraPosition = 0.5;

    @Override
    public void runOpMode() {
        cameraServo = hardwareMap.get(Servo.class, "gate");
        cameraServo.setPosition(cameraPosition);
        tagProcessor = new AprilTagProcessor.Builder().build();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Eyes"))
                .addProcessor(tagProcessor)
                .build();
        waitForStart();

        while (opModeIsActive()) {
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
            sleep(20);
        }
        visionPortal.close();
    }
}
