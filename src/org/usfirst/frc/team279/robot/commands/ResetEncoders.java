package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ResetEncoders extends Command {

    public ResetEncoders() {
    	super("ResetEncoders");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(true);
    }

    protected void initialize() {
    }

    protected void execute() {
    	Robot.mecanumDrive.getEncoderLeftFront().reset();
    	Robot.mecanumDrive.getEncoderRightFront().reset();
    	Robot.mecanumDrive.getEncoderLeftRear().reset();
    	Robot.mecanumDrive.getEncoderRightRear().reset();
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
