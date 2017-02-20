package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CloseGearDoor extends Command {

	private boolean started = false;
	
    public CloseGearDoor() {
        requires(Robot.geargizmo);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	started = true;
    	Robot.geargizmo.resetCloseSwitch();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(!started){
    		this.initialize();
    	}
    	if(!Robot.geargizmo.getCloseCount()) {
    		Robot.geargizmo.closeDoor();
    	} else {
    		Robot.geargizmo.stopDoor();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(Robot.geargizmo.getCloseCount()){
        	Robot.geargizmo.resetOpenSwitch();
    		return true;
    	}
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	started = false;
    	Robot.geargizmo.stopDoor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	started = false;
    	Robot.geargizmo.stopDoor();
    }
}
