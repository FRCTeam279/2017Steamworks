package org.usfirst.frc.team279.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CamLightsGear extends Subsystem {

	public DigitalOutput gearLightLow;
	public DigitalOutput gearLightHigh;

	public void init(){
		gearLightLow = new DigitalOutput(21);
		gearLightHigh = new DigitalOutput(22);
		
		gearLightLow.set(false);
		gearLightHigh.set(false);
	}
	
    public void initDefaultCommand() {
    	
    }
}

