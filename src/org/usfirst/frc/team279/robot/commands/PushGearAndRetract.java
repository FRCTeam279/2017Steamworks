package org.usfirst.frc.team279.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PushGearAndRetract extends CommandGroup {

    public PushGearAndRetract() {
        this.addSequential(new GearPusherPush(), 2.0);
        this.addSequential(new Delay(1000));
        this.addSequential(new GearPusherRetract(), 2.0);
    }
}
