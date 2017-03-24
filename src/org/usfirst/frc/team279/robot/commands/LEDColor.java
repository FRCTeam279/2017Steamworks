package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.robot.subsystems.LedStrip;
import org.usfirst.frc.team279.util.DotStarsLEDStrip;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class LEDColor extends Command {
	
	private DotStarsLEDStrip LEDStrip;
	private NetworkTable ntBoiler;
	private NetworkTable ntGear;
	private int numLEDs;
	private DotStarsLEDStrip strip;
    public LEDColor() {
        // Use requires() here to declare subsystem dependencies
       requires(Robot.ledStrip);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	ntBoiler = NetworkTable.getTable("Boiler");
    	ntGear = NetworkTable.getTable("Gear");
    	LEDStrip =  Robot.ledStrip.getLEDStrip();
    	numLEDs = Robot.ledStrip.getNumLEDS();
    	strip = Robot.ledStrip.getLEDStrip();
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	 double  boilerAngle = ntBoiler.getNumber("angle", 0.0);
    	 double gearAngle = ntGear.getNumber("angle", 0.0);
    	 boolean seeBoiler = ntBoiler.getBoolean("eyes", false);
    	 boolean seeGear = ntGear.getBoolean("eyes", false);
    	 
    	 
    	 	 
    	 if(!seeBoiler && !seeGear){
    		    DriverStation.Alliance a = DriverStation.getInstance().getAlliance();
    		    
    		    if(DriverStation.getInstance().isOperatorControl()){
    		    	for(int i = 0; i < numLEDs; i++) {
		        		strip.setLEDColor(i,  0.0, 0.0,  0.0);
		        	}
    		    	return;
    		    } else {
    		    	if(a == DriverStation.Alliance.Blue){
    		    		for(int i = 0; i < numLEDs; i++) {
    		        		// red
    		        		// blue 
    		        		// green
    		        		
    		        		//blue
    		        		strip.setLEDColor(i,  0.0, 0.4,  0.1);
    		        	}
    		    		return;
        		 	} else {
        		 		for(int i = 0; i < numLEDs; i++) {
        		    		// red
        		    		// blue 
        		    		// green
        		    		
        		    		//blue
        		    		//strip.setLEDColor(i,  0.0, 0.5,  0.2);
        		    		
        		    		strip.setLEDColor(i,  0.3, 0.0,  0.0);
        		    	}
        		 		return;
        		 	}
    		    }
    	 } else if(seeBoiler || seeGear){
    		 if ((seeBoiler && Math.abs(boilerAngle) < 5) || (seeGear && Math.abs(gearAngle) < 5)) {
        		 for(int i = 0; i < numLEDs; i++) {
     	    		strip.setLEDColor(i,  0.0, 0.0,  0.3);	
        		 }
        		 return;
    		 } else if (((seeBoiler && Math.abs(boilerAngle) < 15) || (seeGear && Math.abs(gearAngle) < 15))
    				  && ((seeBoiler && Math.abs(boilerAngle) > 5) || (seeGear && Math.abs(gearAngle) > 5))
    				 ) {
    			 for(int i = 0; i < numLEDs; i++) {
      	    		strip.setLEDColor(i,  0.3, 0.3 ,  0.0);	
         		 } 
    			 return;
    	 	 } else {
        		 for(int i = 0; i < numLEDs; i++) {
     	    		strip.setLEDColor(i,  0.3, 0.0 ,  0.0);	
        		 } 
        		 return;
    		}
    	 }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}