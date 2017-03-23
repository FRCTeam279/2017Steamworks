package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRightGear extends CommandGroup {

    public AutoRightGear() {
    	
    	Robot.getAhrs().reset();
    	
    	
    	//9.55pulses per inch on prod going ahead (4"/120p).. ~half that going sideways
    	//13.26pulses per inch on mule (was 6"/250)
    	addSequential(new GearCamLightToggleHigh());
    	
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -600, 0.001, 0, 0, 20, 0.2, 1.0, -10000, 10000), 3.5);
    	addSequential(new YawPID(-145, 0.008, 0, 0, 2, .2), 2.5);
    	
    	
    	if(Robot.getSetForTesting()){
    		addSequential(new Delay(200));
        	addSequential(new RotateToCenterVisionTarget("Gear", "angle", 0.008, 0.0001, 0.0, 3.0, 0.18), 1.5);
        	
    		addSequential(new CloseGearDoor(), 0.5);
	    	addSequential(new OpenGearDoor(), 1.5);
	    	
	    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 3.75, 0.03, 0.00001, 0.0, 0.75, 0.3), 2.0);
	    	
	    	addSequential(new Delay(1500));
	    	
	    	//addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderRightFront(), 90, -400, 0.004, 0, 0, 20, 0.2, 1.0, -10000, 10000), 3.0);
    	}
    	addSequential(new GearCamLightToggleHigh());
    }
}
