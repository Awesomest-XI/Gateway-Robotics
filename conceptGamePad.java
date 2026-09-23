package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "conceptGamePad (Blocks to Java)")
public class conceptGamePad extends LinearOpMode {

  /**
   * This OpMode illustrates using the touchpad feature found on some gamepads.
   *
   * The Sony PS4 gamepad can detect two distinct touches on the central touchpad.
   * Other gamepads with different touchpads may provide mixed results.
   *
   * The touchpads are accessed through the Gamepad blocks.
   * Several new blocks were added to the Gamepad category in FTC SDK Rev 7.
   *     TouchpadFinger1     returns true if at least one finger is detected.
   * TouchpadFinger1X finger 1 X coordinate. Valid if TouchpadFinger1 is true
   * TouchpadFinger1Y finger 1 Y coordinate. Valid if TouchpadFinger1 is true
   *     TouchpadFinger2     returns true if a second finger is detected
   * TouchpadFinger2X finger 2 X coordinate. Valid if TouchpadFinger2 is true
   * TouchpadFinger2Y finger 2 Y coordinate. Valid if TouchpadFinger2 is true
   *
   * Finger touches are reported with an X and Y coordinate in following coordinate system.
   *
   *   1) X is the Horizontal axis, and Y is the vertical axis.
   *   2) The 0,0 origin is at the center of the touchpad.
   *   3) 1.0, 1.0 is at the top right corner of the touchpad.
   *   4) -1.0,-1.0 is at the bottom left corner of the touchpad.
   */
  @Override
  public void runOpMode() {
    telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
    telemetry.addData(">", "Press Start");
    telemetry.update();
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        // Put loop blocks here.
        // Display finger 1 x & y position if finger detected
        if (gamepad1.touchpad_finger_1) {
          telemetry.addData("Finger 1", "x=" + JavaUtil.formatNumber(gamepad1.touchpad_finger_1_x, 2) + " y=" + JavaUtil.formatNumber(gamepad1.touchpad_finger_1_y, 2));
        } else {
          telemetry.addData("Finger 1", "");
        }
        // Display finger 2 x & y position if finger detected
        if (gamepad1.touchpad_finger_2) {
          telemetry.addData("Finger 2", "x=" + JavaUtil.formatNumber(gamepad1.touchpad_finger_2_x, 2) + " y=" + JavaUtil.formatNumber(gamepad1.touchpad_finger_2_y, 2));
        } else {
          telemetry.addData("Finger 2", "");
        }
        telemetry.update();
        sleep(10);
      }
    }
  }
}
