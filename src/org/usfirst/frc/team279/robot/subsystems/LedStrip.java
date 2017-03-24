package org.usfirst.frc.team279.robot.subsystems;

import org.usfirst.frc.team279.robot.commands.LEDColor;
import org.usfirst.frc.team279.util.DotStarsLEDStrip;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class LedStrip extends Subsystem {

    private DotStarsLEDStrip strip = null; 
    public DotStarsLEDStrip getLEDStrip(){
    	return strip;
    }
    
    private int numLEDs = 21;
    public int getNumLEDS(){
    	return numLEDs;
    }
    
    
    
    public void init(){
    	strip = new DotStarsLEDStrip(numLEDs);
    	
    	for(int i = 0; i < numLEDs; i++) {
    		// red
    		// blue 
    		// green
    		
    		//blue
    		strip.setLEDColor(i,  0.0, 0.5,  0.2);
    		
    		//strip.setLEDColor(i,  0.3, 0.0,  0.0);
    	}
    	
    }
    
    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new LEDColor());
    }
}

