package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name="Auton Mark I.", group="Auto")
public class AutonMarkOne extends LinearOpMode {
    private MecanumDrive drive;
    private Action currentAction = null;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize drive
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        waitForStart();

        // Example sequential actions
        currentAction = drive.actionBuilder(drive.pose)
                // Drive forward 24 inches
                .lineToX(24)
                // Turn 90 degrees
                .turnTo(Math.PI/2)
                // Strafe right 12 inches
                .lineToY(12)
                // Follow a spline path
                .splineTo(new Vector2d(48, 24), Math.PI/4)
                .build();

        // Run the action
        //         while (opModeIsActive() && !isStopRequested() && currentAction.run(telemetry.log())) {

        while (opModeIsActive() && !isStopRequested() && currentAction.run((TelemetryPacket) telemetry.log())) {
            // Update drive
            drive.updatePoseEstimate();

            // Optional: Add telemetry
            telemetry.addData("x", drive.pose.position.x);
            telemetry.addData("y", drive.pose.position.y);
            telemetry.addData("heading", Math.toDegrees(drive.pose.heading.toDouble()));
            telemetry.update();
        }
    }
}