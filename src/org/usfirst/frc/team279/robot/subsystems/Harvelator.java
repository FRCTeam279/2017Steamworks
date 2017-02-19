package org.usfirst.frc.team279.robot.subsystems;

import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.util.Config;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Harvelator extends Subsystem {
	private String prefPrefix = "he_";
	
	private int harvelatorPort = 5;
	private Talon harvelator;
	
	
	public void loadPrefs(){
		Config c = new Config();
		
		harvelatorPort = c.load(prefPrefix + "Harvelator", harvelatorPort);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void init() throws RuntimeException {
		System.out.println("HE: Harvelator Init Starting");
		
		loadPrefs();
		System.out.println("HE: Preferences loaded");
		
		harvelator = new Talon(harvelatorPort);
		
		System.out.println("HE: Harvelator Init Complete");	
	}
    
    public void setHarvelator(double spd){
		harvelator.set(spd);
	}
    
    public void stopHarvelator(){
    	harvelator.set(0.0);
    }
    
}

