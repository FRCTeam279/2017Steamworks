package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Feed extends Command {

    public Feed() {
        requires(Robot.feeder);
    }

    protected void initialize() {}

    protected void execute() {
    	Robot.feeder.feed();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.feeder.stopFeed();
    }

    protected void interrupted() {
    	Robot.feeder.stopFeed();
    }
}
