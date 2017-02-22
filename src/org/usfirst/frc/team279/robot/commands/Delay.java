package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Delay extends Command {
	private long delay = (long)0.0;
	private long endTime = (long)0.0;
	private boolean running = false;
	
	
    public Delay(long milliseconds) {
    	super("DriveToUltrasonicDistance");
        this.setInterruptible(true);
        this.setRunWhenDisabled(true);
        delay = milliseconds;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(!running){
    		endTime = System.currentTimeMillis() + delay;
    		running = true;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(System.currentTimeMillis() > endTime) {
    		return true;
    	}
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	running = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	running = false;
    }
}
