package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoMiddleGear extends CommandGroup {

    public AutoMiddleGear() {
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -572, 0.008, 0, 0, 20, 0.2, 1.0, -10000, 10000));
    	//stuff here for placing the gear
    }
}
