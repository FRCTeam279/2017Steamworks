package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoShootingBoilerOnRight extends CommandGroup {

    public AutoShootingBoilerOnRight() {
    	Robot.getAhrs().reset();
    	addSequential(new ShooterCamLightsToggle());
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -400, 0.008, 0, 0, 20, 0.2, 1.0, -10000, 10000));
    	addSequential(new YawPID(137, 0.008, 0, 0, 4, .2));
    	addSequential(new Delay(400));
    	addSequential(new RotateToCenterVisionTarget("Boiler", "pixelOffset", 0.008, 0.0001, 0.0, 2.5, 0.18), 2.5);
    	//addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, 0, 0, 0, 0, 0), 0.5);
    	addParallel(new Shoot(), 7);
    	addSequential(new Feed(), 7);
    	addSequential(new YawPID(0, 0.008, 0, 0, 4, .2));
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderRightFront(), 0, -500, 0.008, 0, 0, 20, 0.2, 1.0, -10000, 10000));
    }
}
