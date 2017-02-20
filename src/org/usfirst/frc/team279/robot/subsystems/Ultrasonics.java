package org.usfirst.frc.team279.robot.subsystems;

import org.usfirst.frc.team279.robot.sensors.UltrasonicsGroup;
import org.usfirst.frc.team279.util.Config;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Ultrasonics extends Subsystem {
	//The preferencesPrefix will be prepended to the preferences loaded from the Robot Preferences
	private String prefPrefix = "us_";
		
	private UltrasonicsGroup ug = UltrasonicsGroup.getInstance();
	public UltrasonicsGroup getUltrasonics(){
		return ug;
	}

	private boolean enabled = false;
		
	private int rangeGearLeftTrig = 11;
	private int rangeGearLeftEcho = 10;
	private int rangeGearRightTrig = 13;
	private int rangeGearRightEcho = 12;
	
	
	public void loadPrefs(){
		Config c = new Config();
		
		rangeGearLeftTrig = c.load(prefPrefix + "rangeGearLeftTrig", rangeGearLeftTrig);
		rangeGearLeftEcho = c.load(prefPrefix + "rangeGearLeftEcho", rangeGearLeftEcho);
		rangeGearRightTrig = c.load(prefPrefix + "rangeGearRightTrig", rangeGearRightTrig);
		rangeGearRightEcho = c.load(prefPrefix + "rangeGearRightEcho", rangeGearRightEcho);
	}

	
	public void init() throws RuntimeException{
		System.out.println("US: Ultrasonics Init Starting");
		
		loadPrefs();
		System.out.println("US: Preferences loaded");
		
		ug.addUltrasonic("rangeGearLeft", rangeGearLeftTrig, rangeGearLeftEcho);
		ug.addUltrasonic("rangeGearRight", rangeGearRightTrig, rangeGearRightEcho);
		//ug.enableUltrasonic("rangeGearLeft");
		//ug.enableUltrasonic("rangeGearRight");
		ug.disableUltrasonic("rangeGearLeft");
		ug.disableUltrasonic("rangeGearRight");
		System.out.println("US: Ultrasonics Init Complete");	
	}
	
		
    public void initDefaultCommand() {
    }
    
	
	public void startUltrasonics(){
		if(enabled){
			return;
		}
		ug.startReading();
		System.out.println("US: Enabled automatic mode for gear ultrasonics");
		enabled = true;
		
		
	}
	
	public void stopUltrasonics(){
		if(!enabled){
			return;
		}
		ug.stopReading();
		System.out.println("US: Disabled automatic mode for gear ultrasonics");	
		enabled = false;
	}
}

