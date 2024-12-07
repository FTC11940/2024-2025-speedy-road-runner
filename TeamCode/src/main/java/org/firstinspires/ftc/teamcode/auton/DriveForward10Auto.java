package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Actions;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "Drive Forward 10 Inches")
public class DriveForward10Auto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize your drive with starting pose
        Pose2d startPose = new Pose2d(0, 0, 0);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        // Initialize dashboard for telemetry
        FtcDashboard dashboard = FtcDashboard.getInstance();

        // Show initial robot setup info
        telemetry.addLine("=== Drive Forward 10 Inches Autonomous ===");
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Start Pose", "X: %.2f, Y: %.2f, Heading: %.2f",
                startPose.position.x,
                startPose.position.y,
                Math.toDegrees(startPose.heading.log()));
        telemetry.update();

        // Create the trajectory action to move forward 10 inches
        Action driveForward = drive.actionBuilder(startPose)
                .lineToX(10) // Move 10 inches forward along the X axis
                .build();

        // Wait for the driver to press start
        telemetry.addData("Status", "Ready to Start - Press Play");
        telemetry.update();
        waitForStart();

        if (isStopRequested()) return;

        // Show that we're starting the trajectory
        telemetry.addData("Status", "Executing trajectory");
        telemetry.update();

        // Create a separate thread for continuous telemetry updates
        Thread telemetryThread = new Thread(() -> {
            while (!isStopRequested() && opModeIsActive()) {
                // Update pose and get current position
                drive.updatePoseEstimate();

                // Update Driver Station telemetry
                telemetry.addData("Current Position", "X: %.2f, Y: %.2f, Heading: %.2f",
                        drive.pose.position.x,
                        drive.pose.position.y,
                        Math.toDegrees(drive.pose.heading.log()));
                telemetry.addData("Distance Remaining", "%.2f inches",
                        Math.abs(10 - drive.pose.position.x));
                telemetry.update();

                // Update Dashboard telemetry
                TelemetryPacket packet = new TelemetryPacket();
                packet.put("x", drive.pose.position.x);
                packet.put("y", drive.pose.position.y);
                packet.put("heading (deg)", Math.toDegrees(drive.pose.heading.log()));
                dashboard.sendTelemetryPacket(packet);

                try {
                    Thread.sleep(50); // Update every 50ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Start telemetry updates
        telemetryThread.start();

        // Execute the trajectory
        Actions.runBlocking(driveForward);

        // Final pose update
        drive.updatePoseEstimate();

        // Show completion status
        telemetry.addData("Status", "Trajectory completed");
        telemetry.addData("Final Position", "X: %.2f, Y: %.2f, Heading: %.2f",
                drive.pose.position.x,
                drive.pose.position.y,
                Math.toDegrees(drive.pose.heading.log()));
        telemetry.update();

        // Wait a moment to show the final telemetry
        sleep(2000);
    }
}