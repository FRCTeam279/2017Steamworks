package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoMiddleGear extends CommandGroup {

    public AutoMiddleGear() {
    	addSequential(new ResetGyro());
    	addSequential(new ResetEncoders());
    	
    	addSequential(new GearCamLightToggleHigh());
    	
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -350, 0.007, 0, 0, 20, 0.2, 1.0, -10000, 10000), 3.0);
    	addSequential(new Delay(350));
    	addSequential(new YawPID(-90, 0.008, 0.00001, 0, 3.0, 0.2, false), 2.5);
    	
    	//if(Robot.getSetForTesting()){
	    	addSequential(new Delay(50));
	    	

	    	addSequential(new OpenGearDoor(), 1.5);
	    	
	        //public RotateToCenterVisionTarget(String table, String key, double p, double i, double d, double tolerance, double minSpeed)
	    	//addSequential(new RotateToCenterVisionTarget("Gear", "pixelOffset", 0.008, 0.0001, 0.0, 2.5, 0.18), 1.5);
	    	
	    	//DriveToUltrasonicDistanceX(String ultrasonicName, double target, double p, double i, double d, double tolerance, double minSpeed)
	    	//addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 10.0, 0.02, 0.00001, 0.0, 4.5, 0.3), 2.5);
	    	
	    	//addSequential(new SquareToUltrasonics("rangeGearLeft", "rangeGearRight", 14.0, 0.008, 0.0, 0.0, 0.5, 0.25), 3.0);
	    	
	    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 3.75, 0.05, 0.00001, 0.0, 0.75, 0.3), 2.0);
	    
	    	addSequential(new Delay(1500));
	    	
	    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 24.0, 0.055, 0.00001, 0.0, 1.0, 0.3), 3.3);
    	//}
    	
    	addSequential(new GearCamLightToggleHigh());
    }
}
