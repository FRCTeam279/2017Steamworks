package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoShooting extends CommandGroup {

    public AutoShooting() {
    	addSequential(new ShooterCamLightsToggle());
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -230, 0.008, 0, 0, 20, 0.2, 1.0, -10000, 10000));
    	addSequential(new YawPID(-137, 0.008, 0, 0, 4, .2));
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, 0, 0, 0, 0, 0), 0.5);
    	addParallel(new Shoot(), 7);
    	addSequential(new Feed(), 7);
    	addSequential(new YawPID(0, 0.008, 0, 0, 4, .2));
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderRightFront(), 0, -500, 0.008, 0, 0, 20, 0.2, 1.0, -10000, 10000));
    }
}
