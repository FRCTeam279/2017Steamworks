package org.usfirst.frc.team279.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PointAtBoiler extends CommandGroup {

    public PointAtBoiler() {
    	addSequential(new ShooterCamLightsToggle());
    	addSequential(new Delay(200));
    	addSequential(new RotateToCenterVisionTarget("Boiler", "pixelOffset", 1280.0, 0.008, 0.0001, 0.0, 3.0, 0.18), 1.25);
    	this.addParallel(new SaveBoilerPic(), 0.5);
    	addSequential(new ShooterCamLightsToggle(), 0.5);
    	
    }
}
