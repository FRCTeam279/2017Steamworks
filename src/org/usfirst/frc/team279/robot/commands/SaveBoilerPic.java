package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class SaveBoilerPic extends InstantCommand {

    public SaveBoilerPic() {
        super();

    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.boilerTable.putBoolean("debug", true);
    }

}
