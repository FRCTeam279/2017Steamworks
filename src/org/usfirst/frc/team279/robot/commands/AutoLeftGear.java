package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoLeftGear extends CommandGroup {

    public AutoLeftGear() {
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -744, 0.001, 0, 0, 20, 0.2, 1.0, -10000, 10000));
    	addSequential(new YawPID(-35, 0.008, 0, 0, 2, .2));
    	//stuff here for placing the gear
    }
}
