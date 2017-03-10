package org.usfirst.frc.team279.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PointAtBoiler extends CommandGroup {

    public PointAtBoiler() {
    	addSequential(new ShooterCamLightsToggle());
    	addSequential(new Delay(175));
    	addSequential(new RotateToCenterVisionTarget("Boiler", "pixelOffset", 0.008, 0.0001, 0.0, 25, 0.18), 1.25);
    	addSequential(new ShooterCamLightsToggle());
    }
}
