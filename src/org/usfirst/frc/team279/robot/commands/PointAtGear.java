package org.usfirst.frc.team279.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PointAtGear extends CommandGroup {

    public PointAtGear() {
    	addSequential(new GearCamLightToggleHigh());
    	addSequential(new Delay(150));
    	addSequential(new RotateToCenterVisionTarget("Gear", "pixelOffset", 0.008, 0.0001, 0.0, 25, 0.18), 1.25);
    	addSequential(new GearCamLightToggleHigh());
    }
}
