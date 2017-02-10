package org.usfirst.frc.team279.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team279.robot.commands.*;
import org.usfirst.frc.team279.util.Attack3Joystick;
import org.usfirst.frc.team279.util.LF310Controller;


public class OI {
	private String prefPrefix = "oi_";

	private boolean debug = false;
	
	
	//--------------------------------------------------------------------------
	// Controllers, Joysticks, TACOS, etc..
	//--------------------------------------------------------------------------
	private int leftDriverStickPort = 0;
	private int rightDriverStickPort = 1;
	private int goControllerPort = 2;
	
	private Joystick leftDriverStick = null;
	private Joystick rightDriverStick = null;
	private Joystick goController = null;
	
	public Joystick getLeftDriverStick(){
		return leftDriverStick;
	}
	public Joystick getRightDriverStick(){
		return rightDriverStick;
	}
	public Joystick getGOController(){
		return goController;
	}
	
	private double leftStickNullZone = 0.15;
	private double rightStickNullZone = 0.15;
	private double goControllerNullZone = 0.15;
	
	public double getLeftStickNullZone() {
		return leftStickNullZone;
	}
	public double getRightStickNullZone() {
		return rightStickNullZone;
	}
	public double getgoControllerNullZone() {
		return goControllerNullZone;
	}
	
	
	//--------------------------------------------------------------------------
	public void readConfig(){
		System.out.println("OI: Reading Config Started");
		
		System.out.println("OI: Reading Config Complete");
	}
	
	public boolean init() {
		System.out.println("OI: Init Started");
		readConfig();
		
		
		try{
			leftDriverStick = new Attack3Joystick(leftDriverStickPort);
		} catch (Exception e) {
			System.out.println("OI: Error instantiating leftDriverStick: " + e.getMessage());
		}
		
		try{
			rightDriverStick = new Attack3Joystick(rightDriverStickPort);
		} catch (Exception e) {
			System.out.println("OI: Error instantiating rightDriverStick: " + e.getMessage());
		}
		
		try{
			goController = new LF310Controller(goControllerPort);
		} catch (Exception e) {
			System.out.println("OI: Error instantiating goController: " + e.getMessage());
		}
		
		
		
		System.out.println("OI: Init Complete");
		return true;   // all good
	}
	
	//--------------------------------------------------------------------------
	
	public double filterInput(double input, double deadzone, double filterFactor) {
		//1. deadzone
		//2. filter
		//	filtered = (fac * x^3) + ((1 - fac)*x)
		// 3. max value
		//https://www.desmos.com/calculator
		//http://www.third-helix.com/2013/04/12/doing-thumbstick-dead-zones-right.html
		
		double sign = 1.0;
		
		if(input < 0.0) { sign = -1.0; } 		
		input = java.lang.Math.abs(input);
		deadzone = java.lang.Math.abs(deadzone);
		
		if(input < deadzone) {
			input = 0.0;
		} else {
			input = input * ((input - deadzone) / (1-deadzone));
		}
		
		double output = (filterFactor * (input * input * input)) + ((1 - filterFactor) * input);
		output = sign * output;
		return output;
	}
	
	//--------------------------------------------------------------------------
	
	
	//--------------------------------------------------------------------------
}
