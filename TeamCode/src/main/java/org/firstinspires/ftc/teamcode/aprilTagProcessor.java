package org.firstinspires.ftc.teamcode.pedro.constants;

import com.bylazar.camerastream.PanelsCameraStream;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.pedro.constants.StreamProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * AprilTag helper for a USB webcam (e.g. Logitech C270). Written for FTC SDK 12.0+.
 *
 * SDK 12 splits detections into two kinds:
 *   - SINGLE tags  (AprilTagSingleDetection)  -> looked up by ID number
 *   - CLUSTERS     (AprilTagClusterDetection) -> a group of tags treated as one target,
 *                                                looked up by name
 *
 * Usage:
 *   aprilTagProcessor vision = new aprilTagProcessor(hardwareMap, "Webcam 1", true);
 *   // every loop:
 *   vision.update();
 *   if (vision.isTagVisible(20)) {
 *       double range = vision.getRange(20);
 *   }
 *
 * All distances are INCHES, all angles are DEGREES.
 * Functions that return a number give Double.NaN when the target isn't visible,
 * so always check isTagVisible(id) / isClusterVisible(name) first.
 */
public class aprilTagProcessor {

    // ---------------- Camera mounting (edit these for your robot) ----------------
    // Where the camera sits relative to the CENTER of the robot, in inches.
    // x = right (+) / left (-), y = forward (+) / back (-), z = up from the floor.
    public static double CAMERA_X = 0;
    public static double CAMERA_Y = 0;
    public static double CAMERA_Z = 0;
    // Which way the camera points. For a camera facing straight forward, level with the floor:
    // yaw = 0, pitch = -90, roll = 0 (this is the FTC SDK's convention).
    public static double CAMERA_YAW = 0;
    public static double CAMERA_PITCH = -90;
    public static double CAMERA_ROLL = 0;

    private final AprilTagProcessor aprilTag;
    private final VisionPortal visionPortal;
    private final boolean streaming;

    private List<AprilTagDetection> detections = new ArrayList<>();
    private final List<AprilTagSingleDetection> singles = new ArrayList<>();
    private final List<AprilTagClusterDetection> clusters = new ArrayList<>();

    /**
     * @param hardwareMap    from your OpMode
     * @param webcamName     name in the robot configuration, e.g. "Webcam 1"
     * @param streamToPanels true = show the camera in Panels' Camera Stream widget
     */
    public aprilTagProcessor(HardwareMap hardwareMap, String webcamName, boolean streamToPanels) {
        aprilTag = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setCameraPose(
                        new Position(DistanceUnit.INCH, CAMERA_X, CAMERA_Y, CAMERA_Z, 0),
                        new YawPitchRollAngles(AngleUnit.DEGREES, CAMERA_YAW, CAMERA_PITCH, CAMERA_ROLL, 0))
                .build();

        // Decimation: higher = faster but shorter detection range. 2 is a good balance.
        aprilTag.setDecimation(2);

        VisionPortal.Builder builder = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, webcamName))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG) // less USB bandwidth next to the Limelight
                .addProcessor(aprilTag)
                .enableLiveView(false);                           // we use Panels instead; saves CPU

        StreamProcessor streamProcessor = null;
        if (streamToPanels) {
            streamProcessor = new StreamProcessor();
            builder.addProcessor(streamProcessor);
        }

        visionPortal = builder.build();

        streaming = streamToPanels;
        if (streamToPanels) {
            PanelsCameraStream.INSTANCE.startStream(streamProcessor, 15);
        }
    }

    // ---------------- Call once per loop ----------------

    /** Grabs the latest detections and sorts them into single tags and clusters. Call ONCE per loop. */
    public void update() {
        detections = aprilTag.getDetections();
        singles.clear();
        clusters.clear();
        for (AprilTagDetection d : detections) {
            if (d instanceof AprilTagSingleDetection) {
                singles.add((AprilTagSingleDetection) d);
            } else if (d instanceof AprilTagClusterDetection) {
                clusters.add((AprilTagClusterDetection) d);
            }
        }
    }

    // ---------------- What can we see? ----------------

    /** Everything seen in the latest frame (single tags and clusters). */
    public List<AprilTagDetection> getDetections() {
        return detections;
    }

    /** Just the single tags. */
    public List<AprilTagSingleDetection> getSingleTags() {
        return singles;
    }

    /** Just the clusters. */
    public List<AprilTagClusterDetection> getClusters() {
        return clusters;
    }

    /** True if any target with pose data (single tag or cluster) is visible. */
    public boolean hasTarget() {
        for (AprilTagDetection d : detections) {
            if (d.ftcPose != null) return true;
        }
        return false;
    }

    /** IDs of every single tag currently visible. */
    public List<Integer> getVisibleIds() {
        List<Integer> ids = new ArrayList<>();
        for (AprilTagSingleDetection d : singles) ids.add(d.id);
        return ids;
    }

    /** Short names of every cluster currently visible. */
    public List<String> getVisibleClusterNames() {
        List<String> names = new ArrayList<>();
        for (AprilTagClusterDetection c : clusters) {
            if (c.metadata != null) names.add(c.metadata.shortName);
        }
        return names;
    }

    /** Full names of every cluster currently visible (e.g. "RED SCORING"). */
    public List<String> getVisibleClusterFullNames() {
        List<String> names = new ArrayList<>();
        for (AprilTagClusterDetection c : clusters) {
            if (c.metadata != null) names.add(c.metadata.name);
        }
        return names;
    }

    /** Short name of the closest visible cluster, or null if none. */
    public String getClosestClusterName() {
        AprilTagClusterDetection closest = null;
        for (AprilTagClusterDetection c : clusters) {
            if (c.metadata == null || c.ftcPose == null) continue;
            if (closest == null || c.ftcPose.range < closest.ftcPose.range) closest = c;
        }
        return closest == null ? null : closest.metadata.shortName;
    }

    /** The closest visible target (single or cluster), or null if none. */
    public AprilTagDetection getClosest() {
        AprilTagDetection closest = null;
        for (AprilTagDetection d : detections) {
            if (d.ftcPose == null) continue;
            if (closest == null || d.ftcPose.range < closest.ftcPose.range) closest = d;
        }
        return closest;
    }

    /** ID of the closest visible single tag, or -1 if none. */
    public int getClosestTagId() {
        AprilTagSingleDetection closest = null;
        for (AprilTagSingleDetection d : singles) {
            if (d.ftcPose == null) continue;
            if (closest == null || d.ftcPose.range < closest.ftcPose.range) closest = d;
        }
        return closest == null ? -1 : closest.id;
    }

    // ---------------- Single tags (by ID) ----------------

    /** True if the single tag with this ID is visible and has pose data. */
    public boolean isTagVisible(int id) {
        return getTag(id) != null;
    }

    /** The detection for one tag, or null if it isn't visible / isn't in the tag library. */
    public AprilTagSingleDetection getTag(int id) {
        for (AprilTagSingleDetection d : singles) {
            if (d.id == id && d.ftcPose != null) return d;
        }
        return null;
    }

    /** Name from the tag library, or "Unknown". */
    public String getTagName(int id) {
        for (AprilTagSingleDetection d : singles) {
            if (d.id == id && d.metadata != null) return d.metadata.name;
        }
        return "Unknown";
    }

    /** Straight-line distance from the camera to the tag (inches). */
    public double getRange(int id)     { return range(getTag(id)); }
    /** Degrees to turn to face the tag (+ = tag is to the LEFT). */
    public double getBearing(int id)   { return bearing(getTag(id)); }
    /** Degrees up/down from the camera's center line (+ = up). */
    public double getElevation(int id) { return elevation(getTag(id)); }
    /** How angled the tag is to the camera (degrees). 0 = you're square to it. */
    public double getYaw(int id)       { return yaw(getTag(id)); }
    /** Sideways distance (inches, + = tag is to the RIGHT). */
    public double getX(int id)         { return x(getTag(id)); }
    /** Forward distance (inches). */
    public double getY(int id)         { return y(getTag(id)); }
    /** Vertical distance (inches, + = tag is ABOVE the camera). */
    public double getZ(int id)         { return z(getTag(id)); }

    /** Robot's field position from this tag (uses CAMERA_* values). Null if not available. */
    public Pose3D getRobotPose(int id) {
        AprilTagSingleDetection d = getTag(id);
        return d == null ? null : d.robotPose;
    }

    // ---------------- Clusters (by name) ----------------

    /** True if the cluster with this short name (or full name) is visible. */
    public boolean isClusterVisible(String name) {
        return getCluster(name) != null;
    }

    /** The detection for one cluster, or null if it isn't visible. */
    public AprilTagClusterDetection getCluster(String name) {
        for (AprilTagClusterDetection c : clusters) {
            if (c.metadata == null || c.ftcPose == null) continue;
            if (name.equals(c.metadata.shortName) || name.equals(c.metadata.name)) return c;
        }
        return null;
    }

    // ---------------- Cluster alliance color ----------------

    /** Which alliance a cluster belongs to. */
    public enum Alliance { RED, BLUE, UNKNOWN }

    /**
     * RED or BLUE based on the cluster's name (BIOBUZZ cluster names start with "R" or "B",
     * e.g. "RED SCORING"). UNKNOWN if the cluster isn't visible or the name doesn't match.
     */
    public Alliance getClusterAlliance(String name) {
        AprilTagClusterDetection c = getCluster(name);
        if (c == null) return Alliance.UNKNOWN;
        return allianceOf(c);
    }

    /** True if this cluster is visible and is a red cluster. */
    public boolean isRedCluster(String name) {
        return getClusterAlliance(name) == Alliance.RED;
    }

    /** True if this cluster is visible and is a blue cluster. */
    public boolean isBlueCluster(String name) {
        return getClusterAlliance(name) == Alliance.BLUE;
    }

    /** True if any cluster of this alliance is visible. */
    public boolean isAllianceClusterVisible(Alliance alliance) {
        return getClosestClusterName(alliance) != null;
    }

    /** Short name of the closest visible cluster of this alliance, or null if none. */
    public String getClosestClusterName(Alliance alliance) {
        AprilTagClusterDetection closest = null;
        for (AprilTagClusterDetection c : clusters) {
            if (c.metadata == null || c.ftcPose == null) continue;
            if (allianceOf(c) != alliance) continue;
            if (closest == null || c.ftcPose.range < closest.ftcPose.range) closest = c;
        }
        return closest == null ? null : closest.metadata.shortName;
    }

    private static Alliance allianceOf(AprilTagClusterDetection c) {
        if (c.metadata == null) return Alliance.UNKNOWN;
        String n = c.metadata.name != null ? c.metadata.name : c.metadata.shortName;
        if (n == null || n.isEmpty()) return Alliance.UNKNOWN;
        char first = Character.toUpperCase(n.trim().charAt(0));
        if (first == 'R') return Alliance.RED;
        if (first == 'B') return Alliance.BLUE;
        return Alliance.UNKNOWN;
    }

    /** Percent of the cluster's tags currently visible (0-100), or -1 if not visible. */
    public int getClusterPercentFound(String name) {
        AprilTagClusterDetection c = getCluster(name);
        return c == null ? -1 : c.percentClusterFound;
    }

    public double getClusterRange(String name)     { return range(getCluster(name)); }
    public double getClusterBearing(String name)   { return bearing(getCluster(name)); }
    public double getClusterElevation(String name) { return elevation(getCluster(name)); }
    public double getClusterYaw(String name)       { return yaw(getCluster(name)); }
    public double getClusterX(String name)         { return x(getCluster(name)); }
    public double getClusterY(String name)         { return y(getCluster(name)); }
    public double getClusterZ(String name)         { return z(getCluster(name)); }

    /** Robot's field position from this cluster. Null if not available. */
    public Pose3D getClusterRobotPose(String name) {
        AprilTagClusterDetection c = getCluster(name);
        return c == null ? null : c.robotPose;
    }

    // ---------------- Shared helpers (work on singles AND clusters) ----------------

    private static double range(AprilTagDetection d)     { return ok(d) ? d.ftcPose.range : Double.NaN; }
    private static double bearing(AprilTagDetection d)   { return ok(d) ? d.ftcPose.bearing : Double.NaN; }
    private static double elevation(AprilTagDetection d) { return ok(d) ? d.ftcPose.elevation : Double.NaN; }
    private static double yaw(AprilTagDetection d)       { return ok(d) ? d.ftcPose.yaw : Double.NaN; }
    private static double x(AprilTagDetection d)         { return ok(d) ? d.ftcPose.x : Double.NaN; }
    private static double y(AprilTagDetection d)         { return ok(d) ? d.ftcPose.y : Double.NaN; }
    private static double z(AprilTagDetection d)         { return ok(d) ? d.ftcPose.z : Double.NaN; }

    private static boolean ok(AprilTagDetection d) {
        return d != null && d.ftcPose != null;
    }

    // ---------------- Measured from the ROBOT'S CENTER instead of the camera ----------------
    // Uses CAMERA_X, CAMERA_Y and CAMERA_YAW to shift the camera's measurement to the
    // center of the robot. Set those constants accurately for these to be correct.

    /**
     * Degrees the ROBOT must turn so its center faces the cluster (+ = target is to the LEFT,
     * same sign as getClusterBearing). Drive this to 0 to aim the robot, not the camera.
     */
    public double getClusterRobotBearing(String name) { return robotBearing(getCluster(name)); }

    /** Straight-line distance (on the floor) from the robot's center to the cluster (inches). */
    public double getClusterRobotRange(String name)   { return robotRange(getCluster(name)); }

    /** Same as getClusterRobotBearing, for a single tag. */
    public double getRobotBearing(int id) { return robotBearing(getTag(id)); }

    /** Same as getClusterRobotRange, for a single tag. */
    public double getRobotRange(int id)   { return robotRange(getTag(id)); }

    /** Target position relative to the robot's center: {x right, y forward} in inches, or null. */
    private static double[] targetInRobotFrame(AprilTagDetection d) {
        if (!ok(d)) return null;
        double yaw = Math.toRadians(CAMERA_YAW);
        double camX = d.ftcPose.x;   // right, from the camera's view
        double camY = d.ftcPose.y;   // forward, from the camera's view
        // Rotate by the camera's mounting angle, then shift by its mounting position
        double robotX = CAMERA_X + camX * Math.cos(yaw) - camY * Math.sin(yaw);
        double robotY = CAMERA_Y + camX * Math.sin(yaw) + camY * Math.cos(yaw);
        return new double[] {robotX, robotY};
    }

    private static double robotBearing(AprilTagDetection d) {
        double[] t = targetInRobotFrame(d);
        if (t == null) return Double.NaN;
        // + = left (matches the SDK's bearing sign); x is + to the right, so flip it
        return Math.toDegrees(Math.atan2(-t[0], t[1]));
    }

    private static double robotRange(AprilTagDetection d) {
        double[] t = targetInRobotFrame(d);
        return t == null ? Double.NaN : Math.hypot(t[0], t[1]);
    }

    // ---------------- Camera settings ----------------

    /** True once the camera is running. Camera controls only work after this. */
    public boolean isCameraReady() {
        return visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING;
    }

    /**
     * Short exposure = less motion blur = tags detected while driving.
     * Typical values: exposureMs 5-6, gain 200-250. Call from init_loop once isCameraReady().
     * @return true if it worked
     */
    public boolean setManualExposure(int exposureMs, int gain) {
        if (!isCameraReady()) return false;

        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
            exposureControl.setMode(ExposureControl.Mode.Manual);
        }
        exposureControl.setExposure(exposureMs, TimeUnit.MILLISECONDS);

        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(gain);
        return true;
    }

    /** Call from your OpMode's stop(). */
    public void close() {
        if (streaming) PanelsCameraStream.INSTANCE.stopStream();
        visionPortal.close();
    }
}