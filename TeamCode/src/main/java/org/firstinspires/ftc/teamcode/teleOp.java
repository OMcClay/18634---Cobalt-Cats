package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

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
    double lastShootTime;
    String gatePos = "closed";
    YawPitchRollAngles robotOrientation;



    @Override
    public void runOpMode() throws InterruptedException {

        driveTrain.initDrive(hardwareMap);
        Operator.initOperator(hardwareMap);
        robotOrientation = driveTrain.imu.getRobotYawPitchRollAngles();


        waitForStart();
        timer.reset();
        while (opModeIsActive()) {
            driver();
            operator();
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
//        if (gamepad1.start) {
//            driveTrain.imu.resetYaw();
//        }
        telemetry.addData("imu", robotOrientation.getYaw(AngleUnit.DEGREES));
        telemetry.update();

        double y = -gamepad1.left_stick_y;
        double x = -gamepad1.right_stick_x;
        double pivot = gamepad1.left_stick_x;

        double yaw = robotOrientation.getYaw(AngleUnit.DEGREES);


        double rotX = x * Math.cos(yaw) + y * Math.sin(yaw);
        double rotY = y * Math.cos(yaw) + x * Math.sin(yaw);
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(pivot), 1);

        driveTrain.frontLeftMotor.setPower(((rotY + rotX + pivot)/denominator) * driveSpeed);
        driveTrain.frontRightMotor.setPower(((rotY - rotX - pivot)/denominator) * driveSpeed);
        driveTrain.backLeftMotor.setPower((-(rotY - rotX + pivot)/denominator) * driveSpeed);
        driveTrain.backRightMotor.setPower((-(rotY + rotX - pivot)/denominator) * driveSpeed);

    }

    public void operator() {
        double targetVelocity = 1300;
        //Intake & transfer speed
        if (!(gamepad2.left_stick_y > 0.1) && !(gamepad2.left_stick_y < 0.1)){
            intakeSpeed = (0.75 * (-gamepad2.left_stick_y));
        }
        else {
            intakeSpeed = (0.75);
        }

        //Fly Wheel on/off


        if ((Operator.flyWheel.getVelocity() > (targetVelocity - 100) && Operator.flyWheel.getVelocity() < (targetVelocity + 100) && !(gamepad2.right_bumper)) || (gamepad2.right_bumper && gatePos == "closed")) {
            Operator.gate.setPosition(Operator.openGate);
            gatePos = "open";
        } else if (!(Operator.flyWheel.getVelocity() > (targetVelocity - 100) && Operator.flyWheel.getVelocity() < (targetVelocity + 100) && !(gamepad2.right_bumper)) || (gamepad2.right_bumper && gatePos == "open")) {
            Operator.gate.setPosition(Operator.closeGate);
            gatePos = "closed";
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

        if (gamepad2.square) {
            if (lastShootTime < timer.seconds() - 1) {
                Operator.transfer.setPower(-0.25);
                Operator.intake.setPower(-0.1);
            } else {
                Operator.flyWheel.setVelocity(1300);
            }

            if (gatePos == "open" && lastShootTime > timer.seconds() - 1) {
                if (!(gamepad2.left_trigger > 0.1)) {
                    Operator.transfer.setPower(-1);
                    Operator.intake.setPower(1);
                } else {
                    Operator.transfer.setPower(gamepad2.left_trigger * intakeSpeed);
                    Operator.intake.setPower(gamepad2.left_trigger * intakeSpeed);
                }

            }
        } else {
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
                } else if (gamepad2.b) {
                    Operator.flyWheel.setVelocity(0);
                }
            }
            Operator.intake.setPower(gamepad2.left_trigger * intakeSpeed);
            Operator.transfer.setPower(gamepad2.left_trigger * intakeSpeed);
            lastShootTime = timer.seconds();
        }

        telemetry.addData("gatePos", Operator.gate.getPosition());
        telemetry.addData("velocity", Operator.flyWheel.getVelocity());
        telemetry.addData("flyWheel", flyWheelStatus);
        telemetry.addData("imu", robotOrientation.getYaw(AngleUnit.DEGREES));
        telemetry.update();
    }

}


