package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRightGear extends CommandGroup {

    public AutoRightGear() {
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -744, 0.00035, 0, 0, 20, 0.15, 1.0, -10000, 10000));
    	addSequential(new YawPID(-45, 0.00035, 0, 0, 5, .2));
    	//stuff here for placing the gear
    }
}
