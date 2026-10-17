package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.List;

@TeleOp (name = "camera1")
public class test1 extends LinearOpMode {
    private Servo cameraServo;
    static final double CAMERA_CENTER = 0.5;

    // Camera tuning
    static final double CAMERA_KD = 0.00015;
    static final double MAX_SERVO_STEP = 0.02;

    double lastBearing = 0;
    double lastTime = 0;

    double cameraPosition = CAMERA_CENTER;

    private static final int DESIRED_ID = 19;
    private static final boolean USE_WEBCAM = true;

    private AprilTagProcessor tagProcessor;
    private AprilTagDetection desired_tag = null;
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() throws InterruptedException {

        boolean targetFound = false;

        cameraServo = hardwareMap.get(Servo.class, "gate");

        initAprilTag();

        waitForStart();

        while (opModeIsActive()) {

            List<AprilTagDetection> currentDetections = tagProcessor.getDetections();
            targetFound = false;

            for (AprilTagDetection detection : currentDetections) {
                telemetry.addData("Maybe?????", "Present");
                if (detection instanceof AprilTagSingleDetection) {
                    telemetry.addData("YESSSS", "Present");
                    AprilTagSingleDetection sdetection = (AprilTagSingleDetection) detection;
                    if (sdetection.ftcPose != null && (DESIRED_ID <= 0 || sdetection.id == DESIRED_ID)) {
                        swivelCameraToTag(sdetection);
                        targetFound = true;
                        break;
                    }
                }
            }
            telemetry.addData("Present: ", currentDetections.size());
            telemetry.addData("Tag Status", targetFound);
            telemetry.update();
            sleep(5);
        }
    }

    private void swivelCameraToTag(AprilTagDetection tag) {
        if (tag == null) return;

        double now = getRuntime();
        double dt = Math.max(now - lastTime, 0.01);
        double bearingRate = (tag.ftcPose.bearing - lastBearing);

        double correction = bearingRate;

        correction = Range.clip(
                correction,
                -MAX_SERVO_STEP,
                MAX_SERVO_STEP
        );

        double newPosition = Range.clip(
                cameraPosition + correction,
                0.0,
                1.0
        );

        if (Math.abs(newPosition - cameraPosition) > 0.001) {
            cameraPosition = newPosition;
            cameraServo.setPosition(cameraPosition);
        }

        lastBearing = tag.ftcPose.bearing;
        lastTime = getRuntime();

        telemetry.addData("Bearing", "%.2f", tag.ftcPose.bearing);
        telemetry.addData("Servo Pos", "%.3f", cameraPosition);
    }

    private void initAprilTag() {
        tagProcessor = new AprilTagProcessor.Builder().build();

        tagProcessor.setDecimation(2);

        if (USE_WEBCAM) {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Eyes"))
                    .addProcessor(tagProcessor)
                    .build();
        } else {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.BACK)
                    .addProcessor(tagProcessor)
                    .build();
        }
    }
}
