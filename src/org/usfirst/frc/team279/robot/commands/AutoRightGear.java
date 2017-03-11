package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRightGear extends CommandGroup {

    public AutoRightGear() {
    	//9.55pulses per inch on prod going ahead (4"/120p).. ~half that going sideways
    	//13.26pulses per inch on mule (was 6"/250)
    	addSequential(new GearCamLightToggleHigh());
    	
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -825, 0.001, 0, 0, 20, 0.2, 1.0, -10000, 10000), 3.5);
    	addSequential(new YawPID(-145, 0.008, 0, 0, 2, .2), 2.5);
    	//stuff here for placing the gear
    	
    	if(Robot.getSetForTesting()){
	    	addSequential(new Delay(150)); 
	    	
	    	
	        //public RotateToCenterVisionTarget(String table, String key, double p, double i, double d, double tolerance, double minSpeed)
	    	addSequential(new RotateToCenterVisionTarget("Gear", "pixelOffset", 0.008, 0.0001, 0.0, 25, 0.18), 2.5);
	    	
	    	//DriveToUltrasonicDistanceX(String ultrasonicName, double target, double p, double i, double d, double tolerance, double minSpeed)
	    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 14.0, 0.006, 0.0, 0.0, 0.5, 0.3), 3.0);
	    	
	    	addSequential(new SquareToUltrasonics("rangeGearLeft", "rangeGearRight", 14.0, 0.008, 0.0, 0.0, 0.5, 0.25), 2.5);
	    	
	    	addSequential(new OpenGearDoor(), 1.5);
	    	
	    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 4.0, 0.006, 0.0, 0.0, 0.5, 0.3), 3.0);
	    	
	    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 24.0, 0.008, 0.0, 0.0, 0.5, 0.3), 3.0);
    	}
    	addSequential(new GearCamLightToggleHigh());
    }
}
