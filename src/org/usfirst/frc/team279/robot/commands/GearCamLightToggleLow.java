package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.robot.subsystems.CamLightsGear;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GearCamLightToggleLow extends Command {

    public GearCamLightToggleLow() {
    	super("GearCamLightToggleLow");
        requires(Robot.mecanumDrive);
        this.setInterruptible(true);
        this.setRunWhenDisabled(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.camLightsGear.gearLightLow.set(!Robot.camLightsGear.gearLightLow.get());
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
