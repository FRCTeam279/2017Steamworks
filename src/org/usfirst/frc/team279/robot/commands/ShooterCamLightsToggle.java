package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterCamLightsToggle extends Command {

    public ShooterCamLightsToggle() {
    	super("ShooterCamLightsToggle");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.camLightShooter.getRelay().get() != Value.kOff) {
    		Robot.camLightShooter.getRelay().set(Value.kOff);
    		System.out.println("ShooterCamLightsToggle: Turning OFF");
    	} else {
    		System.out.println("ShooterCamLightsToggle: Turning ON");
    		Robot.camLightShooter.getRelay().set(Value.kForward);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
