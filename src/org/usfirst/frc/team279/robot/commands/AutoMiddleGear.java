package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoMiddleGear extends CommandGroup {

    public AutoMiddleGear() {
    	addSequential(new GearCamLightToggleHigh());
    	
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -400, 0.008, 0, 0, 20, 0.2, 1.0, -10000, 10000), 3.5);
    	addSequential(new YawPID(-92, 0.008, 0, 0, 2, .2), 2.5);
    	
    	if(Robot.getSetForTesting()){
	    	addSequential(new Delay(300));
	    	
	        //public RotateToCenterVisionTarget(String table, String key, double p, double i, double d, double tolerance, double minSpeed)
	    	addSequential(new RotateToCenterVisionTarget("Gear", "pixelOffset", 0.008, 0.0001, 0.0, 25, 0.18), 2.5);
	    	
	    	//DriveToUltrasonicDistanceX(String ultrasonicName, double target, double p, double i, double d, double tolerance, double minSpeed)
	    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 14.0, 0.006, 0.0, 0.0, 0.5, 0.3), 4.0);
	    	
	    	addSequential(new SquareToUltrasonics("rangeGearLeft", "rangeGearRight", 14.0, 0.008, 0.0, 0.0, 0.5, 0.25), 3.0);
	    	
	    	addSequential(new OpenGearDoor(), 1.5);
	    	
	    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 4.0, 0.006, 0.0, 0.0, 0.5, 0.3), 4.0);
	    
	    	addSequential(new Delay(300));
	    	
	    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 24.0, 0.008, 0.0, 0.0, 0.5, 0.3), 4.0);
    	}
    	
    	addSequential(new GearCamLightToggleHigh());
    }
}
