package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Roboticstest2 (Blocks to Java)")
public class Roboticstest2 extends LinearOpMode {

  private DcMotor shooter_motor;
  private CRServo intakeservo;
  private DcMotor back_right_motor;
  private DcMotor back_left_motor;
  private DcMotor intake_motors;
  private Servo pokerservo;

  int targetVelocity;

  /**
   * triangle is y 
   *square=x
   *x=a
   *circle=b
   */
  private void shooter_motor2() {
    if (gamepad2.triangle) {
      ((DcMotorEx) shooter_motor).setVelocity(targetVelocity);
    } else if (gamepad2.square) {
      shooter_motor.setPower(-0.55);
    } else if (gamepad2.left_bumper) {
      ((DcMotorEx) shooter_motor).setVelocity(targetVelocity);
      if (((DcMotorEx) shooter_motor).getVelocity() >= targetVelocity - 40 && ((DcMotorEx) shooter_motor).getVelocity() <= targetVelocity + 40) {
        intakeservo.setPower(1);
      }
    } else if (gamepad2.dpad_up) {
      intakeservo.setPower(1);
    } else if (gamepad2.dpad_down) {
      intakeservo.setPower(-1);
    } else {
      shooter_motor.setPower(0);
      intakeservo.setPower(0);
    }
  }

  /**
   * This OpMode offers Tank Drive style TeleOp control for a direct drive robot.
   *
   * In this Tank Drive mode, the left and right joysticks (up
   * and down) drive the left and right motors, respectively.
   */
  @Override
  public void runOpMode() {
    shooter_motor = hardwareMap.get(DcMotor.class, "shooter_motor");
    intakeservo = hardwareMap.get(CRServo.class, "intakeservo");
    back_right_motor = hardwareMap.get(DcMotor.class, "back_right_motor");
    back_left_motor = hardwareMap.get(DcMotor.class, "back_left_motor");
    intake_motors = hardwareMap.get(DcMotor.class, "intake_motors");
    pokerservo = hardwareMap.get(Servo.class, "pokerservo");

    // Reverse one of the drive motors.
    // You will have to determine which motor to reverse for your robot.
    // In this example, the right motor was reversed so that positive
    // applied power makes it move the robot in the forward direction.
    back_right_motor.setDirection(DcMotor.Direction.REVERSE);
    double poker = 0.5;
    boolean toggle = false;
    targetVelocity = 1160;
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        // Put loop blocks here.
        shooter_motor2();
        // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
        // We negate this value so that the topmost position corresponds to maximum forward power.
        back_left_motor.setPower(-gamepad1.left_stick_y);
        back_right_motor.setPower(-gamepad1.right_stick_y);
        telemetry.addData("Left Pow", back_left_motor.getPower());
        telemetry.addData("Right Pow", back_right_motor.getPower());
        telemetry.addData("Shooter Velocity", ((DcMotorEx) shooter_motor).getVelocity());
        telemetry.addData("Target Velocity", targetVelocity);
        telemetry.update();
        if (gamepad1.left_trigger > 0.3) {
          intake_motors.setPower(1);
        } else {
          intake_motors.setPower(0);
        }
        if (gamepad2.left_bumper) {
          intakeservo.setPower(1);
        } else {
          intakeservo.setPower(0);
        }
      if (gamepad2.b & !toggle) {
      poker = (poker == 0.5) ? 1.0 : 0.5;
      }
       toggle = gamepad2.b;
          pokerservo.setPosition(poker);
       
        
      }
    }
  }
}
