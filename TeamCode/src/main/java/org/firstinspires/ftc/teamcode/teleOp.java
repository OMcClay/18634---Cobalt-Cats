package org.firstinspires.ftc.teamcode;

import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@Config
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
    double lastSquare = 0;
    boolean square;
    public static double targetVelocity;
    double tagDist;
    double tagX;
    double tagY;
    double tagBear;
    double tagYaw;
    double offset = 0;
    boolean tagFound = false;
    String gatePos = "closed";

    // Rotate around point variables
    static final double turnGain = 0.03;
    static final double maxTurn = 0.5;
    static final double aimDead = 1.0;
    static final double aimOff = 0.0;
    static final double rangeGain = 0.06;
    static final double maxRange = 0.4;
    double lastAimError = 0;
    double turnD = 0.001;
    double lastTagTime = -10;
    double holdRange = 0;
    boolean wasLockedOn = false;

    //Alignment with tags variables
    double maxAngleRight;
    double maxAngleLeft;
    float rightTuning = -2;
    float leftTuning = 0;
    float buffer = 0;
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
        if (alliance == "Red") {
            Operator.stripLED.setPattern(RevBlinkinLedDriver.BlinkinPattern.FIRE_MEDIUM);
        } else if (alliance == "Blue") {
            Operator.stripLED.setPattern(RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_OCEAN_PALETTE);
        } else {
            Operator.stripLED.setPattern(RevBlinkinLedDriver.BlinkinPattern.CONFETTI);
        }
        telemetry.addData("Alliance", alliance);
        telemetry.addData("Initialize", "Initialized");
        telemetry.update();

        waitForStart();
        timer.reset();
        while (opModeIsActive()) {
            robotOrientation = driveTrain.imu.getRobotYawPitchRollAngles();
            tagFound = false;
            driver();
            operator();
            verifyAlignment();

//            ledOrange();

        }
    }

    public void driver() {
//         botCentricDrive();
        fieldCentricDrive();
        aprilTag();

        // Drive Speed
        if (gamepad1.right_bumper) {
            if (driveSpeed == 1) {
                driveSpeed = 0.45;
            }
            else {
                driveSpeed = 1;
            }
        }


        //Manually state what alliance robot is
        if (gamepad1.dpad_up) {
            alliance = "Blue";
            blackboard.put("Alliance", "Blue");
        } else if (gamepad1.dpad_down) {
            alliance = "Red";
            blackboard.put("Alliance", "Red");
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
        boolean lockOn = gamepad1.left_bumper;
        if (lockOn) {
            double error = tagBear - aimOff + offset;
            double derivative = error - lastAimError;
            pivot = Math.abs(error) < aimDead ? 0 : Range.clip(-error * turnGain - derivative * turnD, -maxTurn, maxTurn);

            if (Math.abs(y) > 0.1 || !wasLockedOn) {
                holdRange = tagDist;
            } else {
                rotY = Range.clip((tagDist - holdRange) * rangeGain, -maxRange, maxRange);
            }
            lastAimError = error;
        } else {
            lastAimError = 0;
        }
        if (lastTagTime < timer.seconds() + 5 || lockOn) {
            if (lockOn) {

                double error = tagBear - aimOff;
                double derivative = error - lastAimError;
                pivot = Math.abs(error) < aimDead ? 0 : Range.clip(-error * turnGain - derivative * turnD, -maxTurn, maxTurn);
                if (Math.abs(y) > 0.1 || !wasLockedOn) {
                    holdRange = tagDist;
                } else {
                    rotY = Range.clip((tagDist - holdRange) * rangeGain, -maxRange, maxRange);
                }
                lastAimError = error;
            } else {
                lastAimError = 0;
            }
        }
        wasLockedOn = lockOn;
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
        //Gate Control
        if(!square) {
            // manual gate
            if (gamepad2.right_bumper) {
                gatePos = "open";
                Operator.gate.setPosition(Operator.openGate);
            } else {
                gatePos = "closed";
                Operator.gate.setPosition(Operator.closeGate);
            }
        } else {
            if (gamepad2.left_bumper) {
                //Left bumper stops gate being automatically open with square, then manual control gate
                if (gamepad2.right_bumper) {
                    gatePos = "open";
                    Operator.gate.setPosition(Operator.openGate);
                } else {
                    gatePos = "closed";
                    Operator.gate.setPosition(Operator.closeGate);
                }
            } else {
                //Gate will (almost) always be open when square pressed (shooting controlled by transfer)
                if ((targetVelocity != 0) && (Operator.flyWheel.getVelocity() > (targetVelocity - 150) && Operator.flyWheel.getVelocity() < (targetVelocity + 150))) {
                    Operator.gate.setPosition(Operator.openGate);
                    gatePos = "open";
                } else {
                    Operator.gate.setPosition(Operator.closeGate);
                    gatePos = "closed";
                }
            }
        }

        if (!(targetVelocity == 0 ) && (Operator.flyWheel.getVelocity() > (targetVelocity - 30) && Operator.flyWheel.getVelocity() < (targetVelocity + 30))) {
            //Rumble operator controller when shooter up to speed
            gamepad2.rumble(250);
        }

        if (gamepad1.dpad_left) {
            offset -= 0.002;
        } else if (gamepad1.dpad_right) {
            offset += 0.002;
        }

        // Shoot
        if (gamepad2.square) {
            square = true;
            //Target Velocity from equation
            if (lastTagTime < timer.seconds() + 5) {
                if (!(tagDist >= 0)) {
                    targetVelocity = 1180;
                } else {
                    targetVelocity = Operator.velocityM * tagDist + Operator.velocityB;
                }
            }


            //double targetVelocity = 1300;

            if (lastShootTime > timer.seconds() - 0.3) {
                //reverse transfer & shooter if last time square was pressed wasn't too long ago
                Operator.transfer.setPower(-0.3);
                Operator.intake.setPower(-0.15);
                Operator.flyWheel.setVelocity(-1000);
                Operator.gate.setPosition(Operator.closeGate);
            } else {
                //Once reversed, set the velocity to the target velocity
                Operator.flyWheel.setVelocity(targetVelocity);
            }

            //Shoot (after shooters have been reversed and gate is open)
            if (Operator.gate.getPosition() == Operator.openGate && lastShootTime < timer.seconds() - 1.5) {
                if (!(gamepad2.left_trigger > 0.2)) {
                    //Controls shooting using transfer & intake instead of gate
                    if (!(targetVelocity == 0 ) && (Operator.flyWheel.getVelocity() > (targetVelocity - 30) && Operator.flyWheel.getVelocity() < (targetVelocity + 30))){
                        //If up to speed, run intake & transfer to feed into shooting
                        Operator.transfer.setPower(0.85);
                        Operator.intake.setPower(0.85);
                    } else {
                        //If not up to speed, then prevent balls from going in to shoot
                        Operator.transfer.setPower(0);
                        Operator.intake.setPower(0);
                    }

                } else {
                    //Manually control intake and transfer
                    intakeAndTransfer();
                }

            }
            lastSquare = timer.seconds();
        } else {
            square = false;
            if (lastSquare > timer.seconds() - 1) {
                targetVelocity = 0;
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
                } else if (gamepad2.dpad_up) {
                    targetVelocity = 1400;
                    flyWheelStatus = false;
                    flyWheelTime = timer.seconds();
                } else if (gamepad2.dpad_down) {
                    targetVelocity = 1200;
                    flyWheelStatus = false;
                    flyWheelTime = timer.seconds();
                }
                else if (gamepad2.touchpad) {
                    //poop
                    targetVelocity = 900;
                    flyWheelTime = timer.seconds();
                } else if (gamepad2.share) {
                    // reverse
                    targetVelocity = -2100;
                    flyWheelTime = timer.seconds();
                } else if (gamepad2.b) {
                    //stop
//                    Operator.flyWheel.setPower(0);
                    targetVelocity = 0;
                    flyWheelTime = timer.seconds();
                }
                Operator.flyWheel.setVelocity(targetVelocity);
            }
            if (gamepad2.left_bumper) {
                if (!(targetVelocity == 0 ) && (Operator.flyWheel.getVelocity() > (targetVelocity - 30) && Operator.flyWheel.getVelocity() < (targetVelocity + 40))){
                    //If up to speed, run intake & transfer to feed into shooting
                    Operator.transfer.setPower(0.85);
                    Operator.intake.setPower(0.85);
                } else {
                    //If not up to speed, then prevent balls from going in to shoot
                    Operator.transfer.setPower(0);
                    Operator.intake.setPower(0);
                }
            } else {
                intakeAndTransfer();
            }

            lastShootTime = timer.seconds();
        }

        telemetry.addData("velocity", Operator.flyWheel.getVelocity());
        telemetry.addData("Target Velocity", targetVelocity);
        telemetry.addData("imu", robotOrientation.getYaw(AngleUnit.DEGREES));
        telemetry.addData("Distance to cluster", tagDist);
        telemetry.addData("Tag Yaw", tagYaw);
        telemetry.addData("Tag Bearing", tagBear);
        telemetry.update();
    }

    public void intakeAndTransfer() {
        //Intake & transfer speed
        if ((gamepad2.left_stick_y >= 0.1) || (gamepad2.left_stick_y <= -0.1)){
            intakeSpeed = (0.8 * (-gamepad2.left_stick_y));
        }
        else {
            intakeSpeed = (0.8);
        }

        //Whether transfer runs or not
        if(gatePos == "open") {
            Operator.intake.setPower(gamepad2.left_trigger * intakeSpeed);
            Operator.transfer.setPower(gamepad2.left_trigger * intakeSpeed);
        } else {
            if (intakeSpeed < 0) {
                //Lets transfer run backwards no matter what
                Operator.intake.setPower(gamepad2.left_trigger * intakeSpeed);
                Operator.transfer.setPower(gamepad2.left_trigger * intakeSpeed);
            } else {
                //Prevents transfer running forwards
                Operator.intake.setPower(gamepad2.left_trigger * intakeSpeed);
                Operator.transfer.setPower(0);
            }
        }
    }

    public void aprilTag() {
        boolean clusterFound = false;
        for (AprilTagDetection detection : tagProcessor.getDetections()) {
            if (detection instanceof AprilTagClusterDetection) {
                AprilTagClusterDetection detectedCluster = (AprilTagClusterDetection) detection;
                String clusterName = detectedCluster.metadata.name;
                double clusterRoll = detectedCluster.ftcPose.roll;

                //Correct alliance?
                if (clusterName.startsWith("R") && "Red".equals(alliance) || clusterName.startsWith("B") && "Blue".equals(alliance)) {
                    //Is it down or up?
                    if (Math.abs(clusterRoll) < 90) {
                        tagDist = detectedCluster.ftcPose.range;
                        tagX = detectedCluster.ftcPose.x;
                        tagY = detectedCluster.ftcPose.y;
                        tagBear = detectedCluster.ftcPose.bearing;
                        tagYaw = detectedCluster.ftcPose.yaw;
                        tagFound = true;
                        lastTagTime = timer.seconds();

                        break;
                    }
                }
            }
        }
//        verifyAlignment();
        // no telemetry.update() here: operator() already calls it once per loop
    }


    public void verifyAlignment() {
        double yawRad = tagYaw * Math.PI/180;
        double bearRad = tagBear * Math.PI/180;
        maxAngleRight=atan((10*cos(yawRad)+1.4*sin(yawRad))/(tagDist-(10*sin(yawRad)+1.4*cos(yawRad)))) * 180/Math.PI;
        maxAngleLeft=atan((-10*cos(yawRad)-1.4*sin(yawRad))/(tagDist-(-10*sin(yawRad)-1.4*cos(yawRad)))) * 180/Math.PI;
        if(maxAngleRight > maxAngleLeft){
            maxAngleRight-=buffer;
            maxAngleLeft+=buffer;
        } else if(maxAngleLeft > maxAngleRight){
            maxAngleRight+=buffer;
            maxAngleLeft-=buffer;
        }
        maxAngleRight += rightTuning;
        maxAngleLeft += leftTuning;

        if (tagFound) {
            if(maxAngleRight > maxAngleLeft){
                if(tagBear < maxAngleRight && tagBear > maxAngleLeft){
                    ledGreen();
                    telemetry.addData("align", "green");
                    telemetry.update();

                } else {
                    ledRed();
                }
            } else if (maxAngleRight < maxAngleLeft){
                if (tagBear > maxAngleRight && tagBear < maxAngleLeft) {
                    ledGreen();
                    telemetry.addData("align", "green");
                    telemetry.update();
                } else {
                    ledRed();
                }
            } else {
                ledRed();
                telemetry.addData("align", "red");
                telemetry.update();
            }
        } else {
            ledRed();
        }

    }

    public void ledGreen() {
        Operator.stripLED.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
        Operator.leftLEDG.setState(false);
        Operator.leftLEDR.setState(true);
        Operator.rightLEDG.setState(false);
        Operator.rightLEDR.setState(true);
    }

    public void ledRed() {
        Operator.stripLED.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
        Operator.leftLEDG.setState(true);
        Operator.leftLEDR.setState(false);
        Operator.rightLEDG.setState(true);
        Operator.rightLEDR.setState(false);
    }

    public void ledOrange() {
        Operator.stripLED.setPattern(RevBlinkinLedDriver.BlinkinPattern.ORANGE);
        Operator.leftLEDG.setState(false);
        Operator.leftLEDR.setState(false);
        Operator.rightLEDG.setState(false);
        Operator.rightLEDR.setState(false);
    }

    public void ledOff() {
        Operator.stripLED.setPattern(RevBlinkinLedDriver.BlinkinPattern.BREATH_BLUE);
        Operator.leftLEDG.setState(true);
        Operator.leftLEDR.setState(true);
        Operator.rightLEDG.setState(true);
        Operator.rightLEDR.setState(true);
    }
}

//If abs value yaw < 80 or the abs value bearing < 10
//disregard ^^^
// abs value bearing < arctan(    (10*cos(yaw) + 1.4*sin(yaw)) / (distTag - (10*sin(yaw)+1.4cos(yaw)))          and abs value (bearing + yaw) < 80 then good