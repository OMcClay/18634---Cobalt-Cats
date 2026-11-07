package org.firstinspires.ftc.teamcode;
// "package" tells Java which folder this file lives in. Android Studio writes this line
// for you when you create a class. If you put this file in a different folder, this line
// must be changed to match (the easiest way: let Android Studio generate it).

// =====================================================================================
// IMPORTS
// An import is like telling Java: "I'm going to use this tool, go get it for me."
// If a word in your code is red, you are usually missing its import.
// =====================================================================================

// "static" imports let us write line(...) instead of Paths.line(...). They save typing.
import static com.pedropathing.api.Paths.curve;                    // makes a curved path
import static com.pedropathing.api.Paths.line;                     // makes a straight path
import static com.pedropathing.ivy.Scheduler.schedule;             // starts a routine running
import static com.pedropathing.ivy.commands.Commands.instant;      // "do this one thing right now"
import static com.pedropathing.ivy.commands.Commands.waitMs;       // "wait this many milliseconds"
import static com.pedropathing.ivy.groups.Groups.sequential;       // "do these in order"
import static com.pedropathing.ivy.pedro.PedroCommands.follow;     // "drive along this path"

import com.pedropathing.api.PoseFactory;      // a machine that stamps out Poses (positions)
import com.pedropathing.follower.Follower;    // the "driver" that actually moves the robot
import com.pedropathing.ivy.Command;          // one step of the routine (drive, wait, score...)
import com.pedropathing.ivy.Scheduler;        // the "manager" that runs Commands
import com.pedropathing.math.Pose;            // a spot on the field: x, y, and which way you face
import com.pedropathing.paths.Path;           // a route between poses

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; // marks this as an Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode;     // the FTC base class we build on

import org.firstinspires.ftc.teamcode.pedro.Constants;     // YOUR tuned robot settings

// =====================================================================================
// THE CLASS
// A class is a blueprint. This one is our autonomous program.
//
// @Autonomous makes it show up in the Autonomous list on the Driver Hub.
// "extends OpMode" means: "start with all the standard FTC program behavior
// (init, start, loop...) and let me fill in the parts I care about."
// =====================================================================================
@Autonomous(name = "test3Pedr")
public class test3 extends OpMode {

    // ---------------------------------------------------------------------------------
    // FIELDS (variables that live as long as the program runs)
    // ---------------------------------------------------------------------------------

    // The Follower is Pedro's brain. Every loop it asks "where am I, where should I be?"
    // and spins the wheels to correct the difference.
    private Follower follower;

    // A PoseFactory makes Poses. ".degrees()" means we type headings in degrees
    // (0-360), which is easier for humans than radians.
    private final PoseFactory p = PoseFactory.degrees();

    // ---------------------------------------------------------------------------------
    // POSES: named spots on the field.
    // p.of(x, y, heading)
    //   x       = inches from the LEFT wall (bigger = further right)
    //   y       = inches from the BOTTOM wall (bigger = further up the field)
    //   heading = direction the robot faces. 0 = facing right, 90 = facing up,
    //             180 = facing left, 270 = facing down. Counterclockwise is positive.
    // The field is 144 x 144 inches, and (0, 0) is the bottom-left corner.
    //
    // THESE NUMBERS ARE EXAMPLES. Plan your real route in the Pedro Visualizer
    // (https://visualizer.pedropathing.com/) and copy the numbers here.
    // ---------------------------------------------------------------------------------
    private final Pose startPose        = p.of(24, 24, 0);   // where the robot is placed before the match
    private final Pose scorePose        = p.of(48, 48, 45);  // where we score
    private final Pose pickupControlPose = p.of(60, 30, 0);  // a "bend" point that shapes a curve
    private final Pose pickupPose       = p.of(72, 36, 0);   // where we grab a game piece
    private final Pose parkPose         = p.of(24, 72, 90);  // where we finish

    // A simple counter so you can see an "instant" command working in telemetry.
    private int scoredCount = 0;

    // ---------------------------------------------------------------------------------
    // PATHS: routes between poses.
    // Each is a small method that RETURNS a Path (that's what "private Path" means).
    //
    // line(a, b)               straight line from a to b.
    // curve(a, control, b)     smooth curve from a to b, bent toward the control pose.
    // .linear(a, b)            "interpolation": the robot turns smoothly from a's heading
    //                          to b's heading while it drives. (If you add nothing, the
    //                          robot just faces the way it is driving.)
    // ---------------------------------------------------------------------------------
    private Path startToScore() {
        return line(startPose, scorePose).linear(startPose, scorePose);
    }

    private Path scoreToPickup() {
        return curve(scorePose, pickupControlPose, pickupPose).linear(scorePose, pickupPose);
    }

    private Path pickupToScore() {
        return line(pickupPose, scorePose).linear(pickupPose, scorePose);
    }

    private Path scoreToPark() {
        return line(scorePose, parkPose).linear(scorePose, parkPose);
    }

    // ---------------------------------------------------------------------------------
    // COMMANDS: the steps of the routine.
    // A Command is "one thing the robot does" - drive a path, wait, run a mechanism...
    // ---------------------------------------------------------------------------------

    // Placeholder for your scoring mechanism.
    // sequential(...) runs its commands one after another.
    // instant(...) runs a bit of code once and finishes immediately.
    // waitMs(500) pauses 500 milliseconds (half a second).
    //
    // TO MAKE IT REAL: replace the waitMs with whatever moves your arm / claw / shooter.
    private Command score() {
        return sequential(
                instant(() -> scoredCount++),  // "+1 to the counter"
                waitMs(500)                    // pretend the mechanism takes half a second
        );
    }

    // The whole autonomous, read top to bottom like a to-do list.
    // follow(follower, somePath) drives the path and FINISHES when the robot has arrived,
    // so the next line only starts after the previous one is done.
    private Command autoRoutine() {
        return sequential(
                follow(follower, startToScore()),   // 1. drive to the scoring spot
                score(),                            // 2. score
                follow(follower, scoreToPickup()),  // 3. curve over to the game piece
                waitMs(300),                        // 4. placeholder: run your intake here
                follow(follower, pickupToScore()),  // 5. drive back
                score(),                            // 6. score again
                follow(follower, scoreToPark())     // 7. park
        );
    }

    // ---------------------------------------------------------------------------------
    // THE OPMODE LIFECYCLE
    // FTC calls these three methods for you, in this order:
    //   init()   once, when you press INIT
    //   start()  once, when you press the PLAY button
    //   loop()   over and over (many times per second) until time runs out or you press STOP
    // ---------------------------------------------------------------------------------

    @Override
    public void init() {
        // Wipe out any leftover commands from a previous run, so we start clean.
        Scheduler.reset();

        // Build the Follower using YOUR settings from Constants.java
        // (motor names, odometry pod offsets, tuned numbers).
        follower = Constants.create(hardwareMap);

        // Tell the follower where the robot STARTS. If this doesn't match where you
        // really put the robot, every path will be off by the same amount.
        follower.setPose(startPose);
        follower.update();

        telemetry.addLine("Ready. Place the robot exactly on the start pose, then press PLAY.");
        telemetry.update();
    }

    @Override
    public void start() {
        // Hand the finished routine to the Scheduler. From now on, the Scheduler runs it.
        schedule(autoRoutine());
    }

    @Override
    public void loop() {
        // These two lines are the heartbeat of the program. They MUST run every loop.

        // 1) The follower reads the sensors, figures out where the robot is,
        //    and sets the wheel powers.
        follower.update();

        // 2) The Scheduler moves the routine forward: checks if the current step
        //    is finished and starts the next one.
        Scheduler.execute();

        // Telemetry = messages on the Driver Hub screen. Great for debugging.
        telemetry.addData("X (in)", follower.pose().x());
        telemetry.addData("Y (in)", follower.pose().y());
        telemetry.addData("Heading (deg)", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower mode", follower.mode());
        telemetry.addData("Scored", scoredCount);
        telemetry.update();
    }
}