package org.usfirst.frc.team279.robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CamLightsShooter extends Subsystem {
	private int lightsRelayPort = 0;
    private Relay lightsRelay = new Relay(lightsRelayPort);
    
    public Relay getRelay(){
 	   return lightsRelay;
    }
    
    public void initDefaultCommand() {
       
    }
}

