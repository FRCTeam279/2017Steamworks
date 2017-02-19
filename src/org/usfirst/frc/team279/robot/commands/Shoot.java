package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.util.SamplesSystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shoot extends Command {
	
	private SamplesSystem ss;
	private int sampleAmount;

    public Shoot() {
        requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	sampleAmount = (int) SmartDashboard.getNumber("Set Sample Amount", 25);
    	ss = new SamplesSystem(sampleAmount);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.shooter.getEyes()) {
	    	double spd = Robot.shooter.calcSpeedFromAngle();
	    	Robot.shooter.shootRPM(spd);
	    	ss.setSample(spd);
    	} else {
    		if(ss.checkSamples()) {
    			Robot.shooter.shootRPM(ss.getAverage());
    		}
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooter.stopShooter();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.shooter.stopShooter();
    }
}
