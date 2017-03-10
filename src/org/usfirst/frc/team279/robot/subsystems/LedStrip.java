package org.usfirst.frc.team279.robot.subsystems;

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
    		strip.setLEDColor(i,  0, 8 ,  64);	
    	}
    	
    }
    
    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

