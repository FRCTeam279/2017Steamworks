package org.usfirst.frc.team279.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PointAtGear extends CommandGroup {

    public PointAtGear() {
    	addSequential(new GearCamLightToggleHigh());
    	addSequential(new Delay(200));
    	addSequential(new RotateToCenterVisionTarget("Gear", "angle", 0.009, 0.0001, 0.0, 3.0, 0.18), 1.25);
    	addSequential(new GearCamLightToggleHigh());
    }
}
