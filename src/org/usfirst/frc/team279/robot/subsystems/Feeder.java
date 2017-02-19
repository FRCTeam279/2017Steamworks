package org.usfirst.frc.team279.robot.subsystems;

import org.usfirst.frc.team279.util.Config;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Feeder extends Subsystem {
	
	//The preferencesPrefix will be prepended to the preferences loaded from the Robot Preferences
	private String prefPrefix = "fd_";
	
	//Feed Motor Default Speed
	private double  feedSpeed     = 0.0;
	
	//Feed SpeedController
	private SpeedController feedMotor   = null;
	
	//SpeedController Ports
	private int     feedPort      = 0;
	
	//SpeedController Inverts
	private boolean invertFeed    = false;
	
	
	
	//*** INIT *******************************************************
	
    public void init() throws RuntimeException {
    	
    	System.out.println("SH: Shooter Init Starting");
    	
    	loadPrefs();
		System.out.println("SH: Preferences loaded");
		
    	feedMotor = new Talon(feedPort);
		System.out.println("SH: Speed Controllers Setup");
    }

    
    public void initDefaultCommand() {
    }
    
    
    /**
     * Loads the values from the SmartDashboard
     */
    public void loadPrefs() {
		Config c = new Config();
		
		feedPort      = c.load(prefPrefix + "feedPort", feedPort);
		invertFeed    = c.load(prefPrefix + "invertFeed", invertFeed);
		feedSpeed     = c.load(prefPrefix + "feedSpeed", feedSpeed);
	}
    
    
    
    //*** FEED MOTOR *************************************************
    
    /**
     * Run the feed motor forward at the default rate
     */
    public void feed() {
    	feedMotor.set(feedSpeed);
    }
    

    /**
     * Run the feed motor forward at the given rate
     * @param spd Number between -1 and 1
     */
    public void feed(double spd) {
    	feedMotor.set(spd);
    }
    
    
    /**
     * Run the feed motor in reverse at the default rate
     */
    public void reverseFeed() {
    	feedMotor.set(-feedSpeed);
    }
    

    /**
     * Run the feed motor in reverse at the given rate
     * @param spd Number between -1 and 1
     */
    public void reverseFeed(double spd) {
    	feedMotor.set(spd);
    }
    
    
    /**
     * Stops the feed motor
     */
    public void stopFeed() {
    	feedMotor.stopMotor();
    }
}

