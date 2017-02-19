package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CloseGearDoor extends Command {

    public CloseGearDoor() {
        requires(Robot.geargizmo);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.geargizmo.resetOpenSwitch();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.geargizmo.closeDoor();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(Robot.geargizmo.getCloseCount()) {
        	return true;
        } else {
        	return false;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.geargizmo.stopDoor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.geargizmo.stopDoor();
    }
}
