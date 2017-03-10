package org.usfirst.frc.team279.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class UnloadGear extends CommandGroup {

    public UnloadGear() {
    	addSequential(new SquareToUltrasonics("rangeGearLeft", "rangeGearRight", 14.0, 0.008, 0.0, 0.0, 0.5, 0.25), 3.0);
    	
    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 14.0, 0.006, 0.0, 0.0, 0.5, 0.3), 3.0);
    	
    	addSequential(new OpenGearDoor(), 1.5);
    	
    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 4.0, 0.006, 0.0, 0.0, 0.5, 0.3), 3.0);
    	
    	addSequential(new Delay(300));
    	
    	addSequential(new DriveToUltrasonicDistanceX(new String[] { "rangeGearLeft", "rangeGearRight" }, 24.0, 0.008, 0.0, 0.0, 0.5, 0.3), 3.0);
    	
    }
}
