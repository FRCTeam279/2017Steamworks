package org.usfirst.frc.team279.robot.subsystems;

import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.robot.commands.StopClimber;
import org.usfirst.frc.team279.util.Config;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {
	private String prefPrefix = "cl_";
	
	private int climberPort = 4;
	private Victor climberSpdCtl;
	
	
	public void loadPrefs(){
		Config c = new Config();
		
		climberPort = c.load(prefPrefix + "climberPort", climberPort);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new StopClimber());
    }
    
    public void init() throws RuntimeException {
		System.out.println("CL: Climber Init Starting");
		
		loadPrefs();
		System.out.println("CL: Preferences loaded");
		
		climberSpdCtl = new Victor(climberPort);
		
		System.out.println("CL: Climber Init Complete");	
	}
    
    public void setClimber(double spd){
    	climberSpdCtl.set(spd);
	}
    
    public void stopClimber(){
    	climberSpdCtl.set(0.0);
    }
    
}

