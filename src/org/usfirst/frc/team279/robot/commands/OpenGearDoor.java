package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;


public class OpenGearDoor extends Command {
	
    public OpenGearDoor() {
        requires(Robot.geargizmo);
        this.setTimeout(10);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(!Robot.geargizmo.getOpenCount()) {
    		Robot.geargizmo.openDoor();
    	} else {
    		Robot.geargizmo.stopDoor();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(this.isTimedOut()) {
    		return true;
    	} else if(Robot.geargizmo.getOpenCount()) {
    		return true;
    	} else {
    		return false;
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.geargizmo.stopDoor();
    	Robot.geargizmo.resetCloseSwitch();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.geargizmo.stopDoor();
    	Robot.geargizmo.resetCloseSwitch();
    }
}
