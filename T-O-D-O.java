package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    // -------------------------------
    // AprilTag target settings
    // -------------------------------
    final double DESIRED_DISTANCE = 12.0;   // how close to stop from the tag, in inches
    private static final int DESIRED_TAG_ID = -1;   // -1 means accept any tag

    // -------------------------------
    // Auto movement tuning
    // Higher max speeds, but still controllable
    // -------------------------------
    final double SPEED_GAIN  = 0.020;
    final double STRAFE_GAIN = 0.015;
    final double TURN_GAIN   = 0.010;

    final double MAX_AUTO_SPEED  = 0.80;
    final double MAX_AUTO_STRAFE = 0.75;
    final double MAX_AUTO_TURN   = 0.50;

    // -------------------------------
    // "Good enough" tolerances
    // If robot is within these limits, it stops auto mode
    // -------------------------------
    final double RANGE_TOLERANCE   = 1.5;   // inches
    final double HEADING_TOLERANCE = 3.0;   // degrees
    final double YAW_TOLERANCE     = 3.0;   // degrees

    // -------------------------------
    // Drive motors
    // -------------------------------
    private DcMotor frontLeftDrive  = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive   = null;
    private DcMotor backRightDrive  = null;

    // -------------------------------
    // Camera / AprilTag
    // -------------------------------
    private static final boolean USE_WEBCAM = true;
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;
    private AprilTagDetection desiredTag = null;

    @Override
    public void runOpMode() {

        // Auto mode toggle variables
        boolean autoMode = false;
        boolean lastA = false;

        // Variables for auto movement
        boolean targetFound;
        double drive;
        double strafe;
        double turn;

        // Initialize AprilTag vision
        initAprilTag();

        // Map motors
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeftDrive   = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRightDrive  = hardwareMap.get(DcMotor.class, "back_right_drive");

        // Motor directions
        // These are common sample settings, but test yours
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Optional: reduce motion blur for webcam AprilTag tracking
        if (USE_WEBCAM) {
            setManualExposure(6, 250);
        }

        telemetry.addLine("Controls:");
        telemetry.addLine("Left stick Y = left tank");
        telemetry.addLine("Right stick Y = right tank");
        telemetry.addLine("Triggers = strafe");
        telemetry.addLine("A = toggle AprilTag auto");
        telemetry.addLine("B = cancel auto");
        telemetry.addLine("Press START");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            targetFound = false;
            desiredTag = null;
            drive = 0;
            strafe = 0;
            turn = 0;

            // -------------------------------
            // Look for a desired AprilTag
            // -------------------------------
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

            // -------------------------------
            // Toggle auto mode with A
            // -------------------------------
            boolean currentA = gamepad1.a;
            if (currentA && !lastA) {
                autoMode = !autoMode;
            }
            lastA = currentA;

            // Cancel auto mode with B
            if (gamepad1.b) {
                autoMode = false;
            }

            // -------------------------------
            // AUTO MODE
            // -------------------------------
            if (autoMode && targetFound) {

                double rangeError   = desiredTag.ftcPose.range - DESIRED_DISTANCE;
                double headingError = desiredTag.ftcPose.bearing;
                double yawError     = desiredTag.ftcPose.yaw;

                boolean centered =
                        Math.abs(rangeError)   < RANGE_TOLERANCE &&
                        Math.abs(headingError) < HEADING_TOLERANCE &&
                        Math.abs(yawError)     < YAW_TOLERANCE;

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

            // -------------------------------
            // AUTO MODE ON but no tag found
            // -------------------------------
            else if (autoMode) {
                moveRobot(0, 0, 0);

                telemetry.addLine("AUTO MODE ACTIVE - NO TAG FOUND");
                telemetry.addLine("Drive robot until tag is visible or press B to cancel.");
            }

            // -------------------------------
            // MANUAL MODE
            // Tank drive + trigger strafe
            // -------------------------------
            else {
                double leftPower;
                double rightPower;
                double triggerStrafe;

                // Tank drive
                leftPower = -gamepad1.left_stick_y;
                rightPower = -gamepad1.right_stick_y;

                // Triggers for strafe
                // Right trigger = strafe one direction
                // Left trigger = opposite direction
                triggerStrafe = gamepad1.right_trigger - gamepad1.left_trigger;

                // Build wheel powers for tank + strafe
                double frontLeftPower  = leftPower  - triggerStrafe;
                double backLeftPower   = leftPower  + triggerStrafe;
                double frontRightPower = rightPower + triggerStrafe;
                double backRightPower  = rightPower - triggerStrafe;

                // Normalize wheel powers if any magnitude is above 1.0
                double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
                max = Math.max(max, Math.abs(backLeftPower));
                max = Math.max(max, Math.abs(backRightPower));

                if (max > 1.0) {
                    frontLeftPower  /= max;
                    frontRightPower /= max;
                    backLeftPower   /= max;
                    backRightPower  /= max;
                }

                // Send power to motors
                frontLeftDrive.setPower(frontLeftPower);
                frontRightDrive.setPower(frontRightPower);
                backLeftDrive.setPower(backLeftPower);
                backRightDrive.setPower(backRightPower);

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
            telemetry.update();
            sleep(10);
        }
    }

    /**
     * Move robot using mecanum math.
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
            frontLeftPower  /= max;
            frontRightPower /= max;
            backLeftPower   /= max;
            backRightPower  /= max;
        }

        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);
    }

    /**
     * Initialize AprilTag processor and vision portal.
     */
    private void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder().build();

        // Good middle ground for speed vs detection distance
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

    /**
     * Set webcam exposure manually to reduce motion blur.
     */
    private void setManualExposure(int exposureMS, int gain) {
        if (visionPortal == null) {
            return;
        }

        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting...");
            telemetry.update();

            while (!isStopRequested() &&
                    (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
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
