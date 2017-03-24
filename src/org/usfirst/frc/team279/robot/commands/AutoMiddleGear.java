package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoMiddleGear extends CommandGroup {

    public AutoMiddleGear() {
    	addSequential(new ResetGyro());
    	addSequential(new ResetEncoders());
    	
    	addSequential(new GearCamLightToggleHigh());
    	
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -400, 0.006, 0, 0, 20, 0.2, 1.0, -10000, 10000), 3.0);
    	addSequential(new Delay(350));
    	addSequential(new YawPID(-90, 0.008, 0.00002, 0, 3.0, 0.25, false), 2.5);
    	//addSequential(new Delay(200));
    	//addSequential(new RotateToCenterVisionTarget("Gear", "angle", 0.008, 0.0001, 0.0, 3.0, 0.18), 1.5);
    	
    	//if(Robot.getSetForTesting()){
	    	addSequential(new Delay(100));
	    	addSequential(new CloseGearDoor(), 0.5);
	    	addSequential(new OpenGearDoor(), 1.5);
	    	
	    	//DriveToUltrasonicDistanceX(String ultrasonicName, double target, double p, double i, double d, double tolerance, double minSpeed)
	    	//addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 10.0, 0.02, 0.00001, 0.0, 4.5, 0.3), 2.5);
	    	
	    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 3.75, 0.03, 0.00001, 0.0, 0.75, 0.3), 2.0);
	    	
	    	addSequential(new PushGearAndRetract());
	    	
	    	addSequential(new Delay(1000));
	    	
	    	//addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderRightFront(), 90, -400, 0.004, 0, 0, 20, 0.2, 1.0, -10000, 10000), 3.0);
	    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 24.0, 0.06, 0.00001, 0.0, 1.0, 0.3), 3.3);
    	//}
    	
    	addSequential(new GearCamLightToggleHigh());
    }
}
