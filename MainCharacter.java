package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "MainCharacter (Blocks to Java)")
public class MainCharacter extends LinearOpMode {

  private DcMotor BackRight;
  private DcMotor FrontRight;
  private DcMotor BackLeft;
  private DcMotor FrontLeft;
  private Servo idk;
  private DcMotor shootermoter;
  private DcMotor storagemotor;

  /**
   * This OpMode offers Tank Drive style TeleOp control for a direct drive robot.
   *
   * In this Tank Drive mode, the left and right joysticks (up
   * and down) drive the left and right motors, respectively.
   */
  @Override
  public void runOpMode() {
    double see;

    BackRight = hardwareMap.get(DcMotor.class, "BackRight");
    FrontRight = hardwareMap.get(DcMotor.class, "FrontRight");
    BackLeft = hardwareMap.get(DcMotor.class, "BackLeft");
    FrontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
    idk = hardwareMap.get(Servo.class, "idk");
    shootermoter = hardwareMap.get(DcMotor.class, "shootermoter");
    storagemotor = hardwareMap.get(DcMotor.class, "storagemotor");

    // Reverse one of the drive motors.
    // You will have to determine which motor to reverse for your robot.
    // In this example, the right motor was reversed so that positive
    // applied power makes it move the robot in the forward direction.
    BackRight.setDirection(DcMotor.Direction.REVERSE);
    // You will have to determine which motor to reverse for your robot.
    // In this example, the right motor was reversed so that positive
    // applied power makes it move the robot in the forward direction.
    FrontRight.setDirection(DcMotor.Direction.REVERSE);
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        // Put loop blocks here.
        // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
        // We negate this value so that the topmost position corresponds to maximum forward power.
        BackLeft.setPower(-gamepad1.left_stick_y);
        BackRight.setPower(-gamepad1.right_stick_y);
        // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
        // We negate this value so that the topmost position corresponds to maximum forward power.
        FrontLeft.setPower(-gamepad1.left_stick_y);
        FrontRight.setPower(-gamepad1.right_stick_y);
        // uhhsmth = intake servo
        // idk = poker servo
        idk.setPosition(see);
        if (false) {
          see = 0.16;
        }
        if (false) {
          see = 0.84;
        }
        if (gamepad2.dpad_right) {
          // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
          // We negate this value so that the topmost position corresponds to maximum forward power.
          shootermoter.setPower(-1);
          storagemotor.setPower(-1);
        } else {
          // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
          // We negate this value so that the topmost position corresponds to maximum forward power.
          shootermoter.setPower(-gamepad2.right_stick_y);
          storagemotor.setPower(-gamepad2.right_stick_y);
        }
        telemetry.addData("Left Pow", BackLeft.getPower());
        telemetry.addData("Right Pow", BackRight.getPower());
        telemetry.update();
      }
    }
  }
}
