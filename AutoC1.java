package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;


/*
 * This OpMode illustrates the concept of driving a path based on time.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backward for 1 Second
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */
@Autonomous(name="Robot:Shooter", group="Robot")


public class Shooterauto extends LinearOpMode {

    /* Declare OpMode members. */
   private DcMotor rightB;
  private Servo risshort;
  private DcMotor rightshota;
  private DcMotor leftshota;
  private DcMotor rightF;
  private DcMotor leftB;
  private DcMotor leftF;
  private Servo reaisshort;
    private ElapsedTime     runtime = new ElapsedTime();


    static final double     FORWARD_SPEED = 0.6;
    static final double     TURN_SPEED    = 0.5;

    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
     rightB = hardwareMap.get(DcMotor.class, "rightB");
    risshort = hardwareMap.get(Servo.class, "risshort");
    rightshota = hardwareMap.get(DcMotor.class, "rightshota");
    leftshota = hardwareMap.get(DcMotor.class, "leftshota");
    rightF = hardwareMap.get(DcMotor.class, "rightF");
    leftB = hardwareMap.get(DcMotor.class, "leftB");
    leftF = hardwareMap.get(DcMotor.class, "leftF");
    reaisshort = hardwareMap.get(Servo.class, "reaisshort");

// one of the drive motors.
    // You will have to determine which motor to reverse for your robot.
    // In this example, the right motor was reversed so that positive
    // applied power makes it move the robot in the forward direction.
    rightB.setDirection(DcMotor.Direction.REVERSE);
    rightF.setDirection(DcMotor.Direction.REVERSE);

    risshort.setDirection(Servo.Direction.REVERSE);
    rightshota.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    leftshota.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
 double x = 0;
  double y = 0;
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Step through each leg of the path, ensuring that the OpMode has not been stopped along the way.

        // Step 1:  Drive forward for 3 seconds
         rightF.setPower(1);
            rightB.setPower(1);
            leftF.setPower(1);
            leftB.setPower(1);
             reaisshort.setPosition(0.4);
          risshort.setPosition(0.4);
        
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.22)) {
            telemetry.addData("Path", "Leg 3: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 2:  Spin right for 1.3 seconds
         rightF.setPower(y);
            rightB.setPower(y);
            leftF.setPower(x);
            leftB.setPower(x);
           rightshota.setPower(-0.39);
          leftshota.setPower(0.37);
             reaisshort.setPosition(0.4);
          risshort.setPosition(0.4);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 2)) {
            telemetry.addData("Path", "Leg 3: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 3:  Drive Backward for 1 Second
        rightF.setPower(y);
            rightB.setPower(y);
            leftF.setPower(x);
            leftB.setPower(x);
           rightshota.setPower(-0.40);
          leftshota.setPower(0.38);
             reaisshort.setPosition(0);
          risshort.setPosition(0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Path", "Leg 3: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 4:  Stop
        rightF.setPower(0);
            rightB.setPower(0);
            leftF.setPower(0);
            leftB.setPower(0);
rightshota.setPower(0);
          leftshota.setPower(0);
             reaisshort.setPosition(0.4);
          risshort.setPosition(0.4);
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}
  
