package org.usfirst.frc.team279.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoShoot extends CommandGroup {

    public AutoShoot() {
//        addSequential(new lineupToBoiler());
    	addParallel(new Shoot());
    	addParallel(new Feed());
    }
}
