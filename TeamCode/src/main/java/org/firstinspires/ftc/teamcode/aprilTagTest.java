package org.firstinspires.ftc.teamcode.pedro.constants;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

/**
 * Tests every aprilTagProcessor function, in Panels + on the Driver Hub. (FTC SDK 12.0+)
 *
 * BIOBUZZ has only AprilTag CLUSTERS (one under each HIVE CELL), no single tags,
 * so this mostly uses the cluster functions.
 *
 * Shows full values for EVERY cluster in view. Change targetId / exposure / gain live from
 * Panels' Configurables widget.
 */
@Config
@TeleOp(name = "AprilTag Test")
public class aprilTagTest extends OpMode {

    public static int targetId = 20;                    // single tag to show (none in BIOBUZZ)
    public static int exposureMs = 6;                   // lower = less blur, darker image
    public static int gain = 250;                       // higher = brighter, noisier

    private org.firstinspires.ftc.teamcode.pedro.constants.aprilTagProcessor vision;
    private boolean exposureSet = false;

    @Override
    public void init() {
//        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        vision = new org.firstinspires.ftc.teamcode.pedro.constants.aprilTagProcessor(hardwareMap, "Webcam 1", true);
    }

    @Override
    public void init_loop() {
        // Camera controls only work once the camera is streaming
        if (!exposureSet && vision.isCameraReady()) {
            exposureSet = vision.setManualExposure(exposureMs, gain);
        }
        showTelemetry();
    }

    @Override
    public void loop() {
        showTelemetry();
    }

    private void showTelemetry() {
        vision.update();   // once per loop

        // ----- Overview -----
        telemetry.addData("Camera ready", vision.isCameraReady());
        telemetry.addData("Manual exposure", exposureSet);
        telemetry.addData("Any target", vision.hasTarget());
        telemetry.addData("Visible clusters (short)", vision.getVisibleClusterNames().toString());
        telemetry.addData("Visible clusters (full)", vision.getVisibleClusterFullNames().toString());
        telemetry.addData("Closest cluster", String.valueOf(vision.getClosestClusterName()));
        telemetry.addData("Visible single IDs", vision.getVisibleIds().toString());

        // ----- Full values for every visible cluster -----
        if (vision.getVisibleClusterNames().isEmpty()) {
//            telemetry.debug("---- No clusters visible ----");
        }
        for (String name : vision.getVisibleClusterNames()) {
//            telemetry.debug("---- Cluster: " + name + " ----");
            // Keys start with the cluster name so each cluster gets its own lines (and graph series)
            telemetry.addData(name + " tags found (%)", vision.getClusterPercentFound(name));
            telemetry.addData(name + " range (in)", vision.getClusterRange(name));
            telemetry.addData(name + " bearing (deg)", vision.getClusterBearing(name));
            telemetry.addData(name + " yaw (deg)", vision.getClusterYaw(name));
            telemetry.addData(name + " elevation (deg)", vision.getClusterElevation(name));
            telemetry.addData(name + " X right (in)", vision.getClusterX(name));
            telemetry.addData(name + " Y forward (in)", vision.getClusterY(name));
            telemetry.addData(name + " Z up (in)", vision.getClusterZ(name));
            showRobotPose(name + " ", vision.getClusterRobotPose(name));
        }

        // ----- Single tags (BIOBUZZ has none, kept for other games / test tags) -----
//        panelsTelemetry.debug("---- Single tag " + targetId + " ----");
        if (vision.isTagVisible(targetId)) {
            telemetry.addData("Tag name", vision.getTagName(targetId));
            telemetry.addData("Tag range (in)", vision.getRange(targetId));
            telemetry.addData("Tag bearing (deg)", vision.getBearing(targetId));
            telemetry.addData("Tag yaw (deg)", vision.getYaw(targetId));
            telemetry.addData("Tag elevation (deg)", vision.getElevation(targetId));
            telemetry.addData("Tag X/Y/Z (in)", String.format("%.1f / %.1f / %.1f",
                    vision.getX(targetId), vision.getY(targetId), vision.getZ(targetId)));
            showRobotPose("Tag ", vision.getRobotPose(targetId));
        } else {
//            panelsTelemetry.debug("Not visible");
        }
        telemetry.addData("Closest single ID", vision.getClosestTagId());

        telemetry.update();
    }

    /** Shows the robot's field position, if the pose is available. */
    private void showRobotPose(String prefix, Pose3D robotPose) {
        if (robotPose == null) return;
        telemetry.addData(prefix + "robot field X (in)", robotPose.getPosition().toUnit(DistanceUnit.INCH).x);
        telemetry.addData(prefix + "robot field Y (in)", robotPose.getPosition().toUnit(DistanceUnit.INCH).y);
        telemetry.addData(prefix + "robot heading (deg)", robotPose.getOrientation().getYaw(AngleUnit.DEGREES));
    }

    @Override
    public void stofp() {
        vision.close();
    }
}