package org.usfirst.frc.team279.robot.subsystems;

import org.usfirst.frc.team279.util.Config;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearPusher extends Subsystem {
	private String prefPrefix = "gp_";
	
	private Servo leftServo;
    public Servo getLeftServo() {
    	return leftServo;
    }

    private Servo rightServo;
    public Servo getRightServo() {
    	return rightServo;
    }
    
    private double maxAngle = 180.0;
    
    
    //note that left and right are looking from robot at gear
    private int leftPort = 8;
    private int rightPort = 9;
    private double leftAngleAdjustment = 0.0;
    private double rightAngleAdjustment = 0.0;
    private boolean invertLeft = false;
    private boolean invertRight = true;
    
    public double getLeftAngleAdjustment(){
    	return leftAngleAdjustment;
    }
    public double getRightAngleAdjustment(){
    	return rightAngleAdjustment;
    }
    
    public void loadPrefs(){
		Config c = new Config();
		leftPort = c.load(prefPrefix + "leftPort", leftPort);
		rightPort = c.load(prefPrefix + "rightPort", rightPort);
		
		leftAngleAdjustment = c.load(prefPrefix + "leftAngleAdjustment", leftAngleAdjustment);
		rightAngleAdjustment = c.load(prefPrefix + "rightAngleAdjustment", rightAngleAdjustment);
		
		invertLeft  = c.load(prefPrefix + "invertLeft", invertLeft);
		invertRight = c.load(prefPrefix + "invertLeft", invertRight);
		
		System.out.println("GP: Preferences loaded");
	}
    
    public void init(){
    	
    	System.out.println("CL: Climber Init Starting");
		loadPrefs();
		
		leftServo = new Servo(leftPort);
		rightServo = new Servo(rightPort);
		
		this.retract();
		
		System.out.println("CL: Climber Init Complete");
    }
    
    
    public void push(){
    	leftServo.setAngle(maxAngle);
    	rightServo.setAngle(0.0);
    }
    
    public void retract(){
    	rightServo.setAngle(maxAngle);
    	leftServo.setAngle(0.0);
    	
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

