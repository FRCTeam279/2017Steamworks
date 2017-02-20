package org.usfirst.frc.team279.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;
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
	// Buttons 
	//--------------------------------------------------------------------------
	//driver buttons
	private JoystickButton resetGyroBtn;
	
	//go buttons
	private JoystickButton harvForwardBtn;
	private JoystickButton harvBackwardBtn;
	private double shootBtn;
	private double closeShotBtn;
	private JoystickButton feedBtn;
	private JoystickButton feedReverseBtn; //might not need
	private JoystickButton openGearBtn;
	private JoystickButton closeGearBtn;
	private JoystickButton autoGearBtn;
	
	
	
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
		
		//Drivers Buttons
		resetGyroBtn = new JoystickButton(rightDriverStick, 2);
		
		//GO Buttons
		shootBtn = 0.75;
		closeShotBtn = 0.75;
		harvForwardBtn = new JoystickButton(goController, 1);
		harvBackwardBtn = new JoystickButton(goController, 4);
		feedBtn = new JoystickButton(goController, 3);
		feedReverseBtn = new JoystickButton(goController, 2);
		openGearBtn = new JoystickButton(goController, 8);
		closeGearBtn = new JoystickButton(goController, 7);
		autoGearBtn = new JoystickButton(goController, 5);
		
		
		//Applying buttons to commands
		resetGyroBtn.whenPressed(new ResetGyro());
		
		harvForwardBtn.whenPressed(new RunHarvelatorFWD());
		harvForwardBtn.whenReleased(new StopHarvelator());
		harvBackwardBtn.whenPressed(new RunHarvelatorRWD());
		harvBackwardBtn.whenReleased(new StopHarvelator());
		
		openGearBtn.whenPressed(new OpenGearDoor());
		closeGearBtn.whenPressed(new CloseGearDoor());
		
		feedBtn.toggleWhenPressed(new Feed());	
		feedReverseBtn.toggleWhenPressed(new FeedReverse());
		
		
		System.out.println("OI: Init Complete");
		return true;   // all good
	}
	
	Command shoot = new TempShoot();
	Command stopShoot = new StopShooter();
	public void checkForAxisButtons() {
		
		if(goController.getRawAxis(2) > shootBtn) {
			shoot.start();
		} else if(goController.getRawAxis(3) > closeShotBtn) {
			shoot.start();
		} else {
			stopShoot.start();
		}
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
