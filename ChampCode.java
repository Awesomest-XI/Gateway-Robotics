package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Champ Code")
public class Champ extends LinearOpMode {

  private DcMotor rightB;
  private Servo risshort;
  private DcMotor rightshota;
  private DcMotor leftshota;
  private DcMotor rightF;
  private DcMotor leftB;
  private DcMotor leftF;
  private Servo reaisshort;
  ElapsedTime timer = new ElapsedTime();

    int step = 0;
    boolean autoRunning = false;

  /**
   * This OpMode offers Tank Drive style TeleOp control for a direct drive robot.
   *
   * In this Tank Drive mode, the left and right joysticks (up
   * and down) drive the left and right motors, respectively.
   */
  @Override
  public void runOpMode() {
    rightB = hardwareMap.get(DcMotor.class, "rightB");
    risshort = hardwareMap.get(Servo.class, "risshort");
    rightshota = hardwareMap.get(DcMotor.class, "rightshota");
    leftshota = hardwareMap.get(DcMotor.class, "leftshota");
    rightF = hardwareMap.get(DcMotor.class, "rightF");
    leftB = hardwareMap.get(DcMotor.class, "leftB");
    leftF = hardwareMap.get(DcMotor.class, "leftF");
    reaisshort = hardwareMap.get(Servo.class, "reaisshort");

    // Reverse one of the drive motors.
    // You will have to determine which motor to reverse for your robot.
    // In this example, the right motor was reversed so that positive
    // applied power makes it move the robot in the forward direction.
    rightB.setDirection(DcMotor.Direction.REVERSE);
    risshort.setDirection(Servo.Direction.REVERSE);
    rightshota.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    leftshota.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    // You will have to determine which motor to reverse for your robot.
    // In this example, the right motor was reversed so that positive
    // applied power makes it move the robot in the forward direction.
    rightF.setDirection(DcMotor.Direction.REVERSE);
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        
            // ð Start auto sequence
            if (gamepad2.x && !autoRunning) {
                autoRunning = true;
                step = 1;
                timer.reset();
            }

            // ð¤ Autonomous sequence
            if (autoRunning) {

                switch (step) {

                    case 1:
                        rightF.setPower(1);
            rightB.setPower(1);
            leftF.setPower(1);
            leftB.setPower(1);
             reaisshort.setPosition(0.4);
          risshort.setPosition(0.4);
        
                        if (timer.seconds() > 1.22) {
      rightF.setPower(0);
            rightB.setPower(0);
            leftF.setPower(0);
            leftB.setPower(0);
rightshota.setPower(0);
          leftshota.setPower(0);
             reaisshort.setPosition(0.4);
          risshort.setPosition(0.4);                    
          step = 2;
                            timer.reset();
                        }
                        break;

                    case 2:
                       rightF.setPower(0);
            rightB.setPower(0);
            leftF.setPower(0);
            leftB.setPower(0);
           rightshota.setPower(-0.39);
          leftshota.setPower(0.37);
             reaisshort.setPosition(0.4);
          risshort.setPosition(0.4);
                        if (timer.seconds() > 2) {
      rightF.setPower(0);
            rightB.setPower(0);
            leftF.setPower(0);
            leftB.setPower(0);
rightshota.setPower(-0.39);
          leftshota.setPower(0.37);
             reaisshort.setPosition(0.4);
          risshort.setPosition(0.4);                    
          step = 3;
                            timer.reset();
                        }
                        break;

                    case 3:
                        rightF.setPower(0);
            rightB.setPower(0);
            leftF.setPower(0);
            leftB.setPower(0);
           rightshota.setPower(-0.39);
          leftshota.setPower(0.37);
             reaisshort.setPosition(0);
          risshort.setPosition(0);
                        if (timer.seconds() > 3) {
      rightF.setPower(0);
            rightB.setPower(0);
            leftF.setPower(0);
            leftB.setPower(0);
rightshota.setPower(0);
          leftshota.setPower(0);
             reaisshort.setPosition(0.4);
          risshort.setPosition(0.4);                  
          step = 4;
                            timer.reset();
                        }
                        break;
                    case 4:
      rightF.setPower(0);
            rightB.setPower(0);
            leftF.setPower(0);
            leftB.setPower(0);
rightshota.setPower(0);
          leftshota.setPower(0);
             reaisshort.setPosition(0.4);
          risshort.setPosition(0.4);                 
          if (timer.seconds() > 1) {
      rightF.setPower(0);
            rightB.setPower(0);
            leftF.setPower(0);
            leftB.setPower(0);
rightshota.setPower(0);
          leftshota.setPower(0);
             reaisshort.setPosition(0.4);
          risshort.setPosition(0.4);                     
          step = 5;
                            timer.reset();
                        }
                        break;

                    case 5:
                        // Finished
                        autoRunning = false;
                        step = 0;
                        break;
                }

            } else {
                // ð® Normal driver control when not running auto
        leftB.setPower(gamepad1.right_stick_y);
        rightB.setPower(gamepad1.left_stick_y);
        // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
        // We negate this value so that the topmost position corresponds to maximum forward power.
        leftF.setPower(gamepad1.right_stick_y);
        rightF.setPower(gamepad1.left_stick_y);
        if (gamepad2.dpad_left) {
          reaisshort.setPosition(0);
          risshort.setPosition(0);
        } else {
          reaisshort.setPosition(0.4);
          risshort.setPosition(0.4);
        }
        if (gamepad2.a) {
          rightshota.setPower(-0.42);
          leftshota.setPower(0.4);
        } else if (gamepad2.b) {
          leftshota.setPower(-0.3);
          rightshota.setPower(0.3);
        } else {
          leftshota.setPower(0);
          rightshota.setPower(0);
        }
        telemetry.addData("Left Pow", leftB.getPower());
        telemetry.addData("left shot", leftshota.getPower());
        telemetry.addData("right shot", rightshota.getPower());
        telemetry.addData("rshort", risshort.getPosition());
        telemetry.addData("reashort", reaisshort.getPosition());
        telemetry.addData("Right Pow", rightB.getPower());
        telemetry.update();
            }
      }
    }
  }
}
