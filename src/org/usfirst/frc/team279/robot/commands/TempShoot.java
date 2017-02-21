package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TempShoot extends Command {
	private int i = 0;
    public TempShoot() {
        requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Robot.shooter.shootRPM(11000);
    	Robot.shooter.shootRPM(SmartDashboard.getNumber("Shooter RPM Target", 10000));
    	
    	i++;
    	if(i > 50) {
    		System.out.println("CMD TempShoot: Target " + SmartDashboard.getNumber("Shooter RPM Target", 11111) + ", Current: " + Robot.shooter.getShooterController().getSpeed());
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
