package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TeleOp(name = "Tank Drive AprilTag TeleOp", group = "TeleOp")
public class TankDriveAprilTagTeleOp extends LinearOpMode {

    // Drive motors
    private DcMotorEx rightB;
    private DcMotorEx rightF;
    private DcMotorEx leftB;
    private DcMotorEx leftF;

    // Extra configured hardware
    private Servo risshort;
    private Servo reaisshort;
    private DcMotorEx rightshota;
    private DcMotorEx leftshota;

    ElapsedTime timer = new ElapsedTime();

    // AprilTag settings
    final double DESIRED_DISTANCE = 12.0;
    private static final int DESIRED_TAG_ID = -1;   // -1 = any tag

    // Auto tuning
    final double SPEED_GAIN  = 0.020;
    final double STRAFE_GAIN = 0.015;
    final double TURN_GAIN   = 0.010;

    // Higher auto speeds
    final double MAX_AUTO_SPEED  = 0.80;
    final double MAX_AUTO_STRAFE = 0.75;
    final double MAX_AUTO_TURN   = 0.50;

    // Stop tolerances
    final double RANGE_TOLERANCE   = 1.5;
    final double HEADING_TOLERANCE = 3.0;
    final double YAW_TOLERANCE     = 3.0;

    // Camera / AprilTag
    private static final boolean USE_WEBCAM = true;
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;
    private AprilTagDetection desiredTag = null;

    @Override
    public void runOpMode() {

        boolean autoMode = false;
        boolean lastA = false;

        boolean targetFound;
        double drive;
        double strafe;
        double turn;

        initAprilTag();

        // Hardware map
        rightF = hardwareMap.get(DcMotorEx.class, "rightF");
        leftF = hardwareMap.get(DcMotorEx.class, "leftF");
        rightB = hardwareMap.get(DcMotorEx.class, "rightB");
        leftB = hardwareMap.get(DcMotorEx.class, "leftB");

        // Still mapped even though not used yet
        rightshota = hardwareMap.get(DcMotorEx.class, "rightshota");
        leftshota = hardwareMap.get(DcMotorEx.class, "leftshota");
        risshort = hardwareMap.get(Servo.class, "risshort");
        reaisshort = hardwareMap.get(Servo.class, "reaisshort");

        // Motor directions
        // Test these on your robot. This is just the normal left-reversed setup.
        leftF.setDirection(DcMotor.Direction.REVERSE);
        leftB.setDirection(DcMotor.Direction.REVERSE);
        rightF.setDirection(DcMotor.Direction.FORWARD);
        rightB.setDirection(DcMotor.Direction.FORWARD);

        if (USE_WEBCAM) {
            setManualExposure(6, 250);
        }

        telemetry.addLine("Controls:");
        telemetry.addLine("Left stick Y = left tank");
        telemetry.addLine("Right stick Y = right tank");
        telemetry.addLine("Triggers = strafe");
        telemetry.addLine("A = toggle AprilTag auto");
        telemetry.addLine("B = cancel auto");
        telemetry.update();

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {

            targetFound = false;
            desiredTag = null;
            drive = 0;
            strafe = 0;
            turn = 0;

            // Find AprilTag
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();

            for (AprilTagDetection detection : currentDetections) {
                if (detection.metadata != null) {
                    if ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID)) {
                        targetFound = true;
                        desiredTag = detection;
                        break;
                    }
                }
            }

            // Toggle auto mode with A
            boolean currentA = gamepad1.a;
            if (currentA && !lastA) {
                autoMode = !autoMode;
            }
            lastA = currentA;

            // Cancel auto mode with B
            if (gamepad1.b) {
                autoMode = false;
            }

            // AUTO MODE
            if (autoMode && targetFound) {

                double rangeError = desiredTag.ftcPose.range - DESIRED_DISTANCE;
                double headingError = desiredTag.ftcPose.bearing;
                double yawError = desiredTag.ftcPose.yaw;

                boolean centered =
                        Math.abs(rangeError) < RANGE_TOLERANCE &&
                        Math.abs(headingError) < HEADING_TOLERANCE &&
                        Math.abs(yawError) < YAW_TOLERANCE;

                if (centered) {
                    moveRobot(0, 0, 0);
                    autoMode = false;

                    telemetry.addLine("AUTO: target reached");
                    telemetry.addData("Tag ID", desiredTag.id);
                    telemetry.addData("Range", "%5.1f in", desiredTag.ftcPose.range);
                    telemetry.addData("Bearing", "%3.0f deg", desiredTag.ftcPose.bearing);
                    telemetry.addData("Yaw", "%3.0f deg", desiredTag.ftcPose.yaw);
                } else {
                    drive  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
                    turn   = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
                    strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

                    moveRobot(drive, strafe, turn);

                    telemetry.addLine("AUTO MODE ACTIVE");
                    telemetry.addData("Tag ID", desiredTag.id);
                    telemetry.addData("Range", "%5.1f in", desiredTag.ftcPose.range);
                    telemetry.addData("Bearing", "%3.0f deg", desiredTag.ftcPose.bearing);
                    telemetry.addData("Yaw", "%3.0f deg", desiredTag.ftcPose.yaw);
                    telemetry.addData("Auto Drive", "%5.2f", drive);
                    telemetry.addData("Auto Strafe", "%5.2f", strafe);
                    telemetry.addData("Auto Turn", "%5.2f", turn);
                }
            }

            // Auto on but no tag found
            else if (autoMode) {
                moveRobot(0, 0, 0);

                telemetry.addLine("AUTO MODE ACTIVE - NO TAG FOUND");
                telemetry.addLine("Drive until a tag is visible or press B to cancel.");
            }

            // MANUAL MODE
            else {
                double leftPower = -gamepad1.left_stick_y;
                double rightPower = -gamepad1.right_stick_y;
                double triggerStrafe = gamepad1.right_trigger - gamepad1.left_trigger;

                double frontLeftPower  = leftPower - triggerStrafe;
                double backLeftPower   = leftPower + triggerStrafe;
                double frontRightPower = rightPower + triggerStrafe;
                double backRightPower  = rightPower - triggerStrafe;

                double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
                max = Math.max(max, Math.abs(backLeftPower));
                max = Math.max(max, Math.abs(backRightPower));

                if (max > 1.0) {
                    frontLeftPower /= max;
                    frontRightPower /= max;
                    backLeftPower /= max;
                    backRightPower /= max;
                }

                leftF.setPower(frontLeftPower);
                leftB.setPower(backLeftPower);
                rightF.setPower(frontRightPower);
                rightB.setPower(backRightPower);

                telemetry.addLine("MANUAL MODE");
                telemetry.addData("Left Tank", "%5.2f", leftPower);
                telemetry.addData("Right Tank", "%5.2f", rightPower);
                telemetry.addData("Strafe", "%5.2f", triggerStrafe);

                if (targetFound) {
                    telemetry.addData("Tag Seen", "ID %d", desiredTag.id);
                    telemetry.addData("Range", "%5.1f in", desiredTag.ftcPose.range);
                    telemetry.addData("Bearing", "%3.0f deg", desiredTag.ftcPose.bearing);
                    telemetry.addData("Yaw", "%3.0f deg", desiredTag.ftcPose.yaw);
                } else {
                    telemetry.addLine("No AprilTag currently detected");
                }
            }

            telemetry.addData("Auto Toggle", autoMode ? "ON" : "OFF");
            telemetry.addData("Timer", "%.2f", timer.seconds());
            telemetry.update();
            sleep(10);
        }
    }

    /**
     * x = forward/back
     * y = strafe
     * yaw = turn
     */
    public void moveRobot(double x, double y, double yaw) {
        double frontLeftPower  = x - y - yaw;
        double frontRightPower = x + y + yaw;
        double backLeftPower   = x + y - yaw;
        double backRightPower  = x - y + yaw;

        double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }

        leftF.setPower(frontLeftPower);
        rightF.setPower(frontRightPower);
        leftB.setPower(backLeftPower);
        rightB.setPower(backRightPower);
    }

    private void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder().build();
        aprilTag.setDecimation(2);

        if (USE_WEBCAM) {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTag)
                    .build();
        } else {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.BACK)
                    .addProcessor(aprilTag)
                    .build();
        }
    }

    private void setManualExposure(int exposureMS, int gain) {
        if (visionPortal == null) {
            return;
        }

        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting...");
            telemetry.update();

            while (!isStopRequested() &&
                    visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
                sleep(20);
            }

            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }

        if (!isStopRequested()) {
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
                sleep(50);
            }

            exposureControl.setExposure((long) exposureMS, TimeUnit.MILLISECONDS);
            sleep(20);

            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
            sleep(20);
        }
    }
}
