package org.usfirst.frc.team279.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Ultrasonic;

import javax.print.attribute.standard.MediaSize.Other;

import org.usfirst.frc.team279.util.Config;

import edu.wpi.first.wpilibj.*;



/**
 *
 */
public class MecanumDrive extends Subsystem {

	private RobotDrive roboDrive = null;
	public RobotDrive getRoboDrive() {
		return roboDrive;
	}
	
	//The preferencesPrefix will be prepended to the preferences loaded from the Robot Preferences
	private String prefPrefix = "md_";

	
	//****************************************
	// Speed Controllers
	//****************************************
	private int leftFrontSpeedCtrlPort = 0;
	private int rightFrontSpeedCtrlPort = 2;
	private int leftRearSpeedCtrlPort = 1;
	private int rightRearSpeedCtrlPort = 3;
	
	private boolean invertLeftFront = true;
	private boolean invertRightFront = false;
	private boolean invertLeftRear = true;
	private boolean invertRightRear = false;
	

	private SpeedController leftFrontSpeedCtrl = null;
	private SpeedController rightFrontSpeedCtrl = null;
	private SpeedController leftRearSpeedCtrl = null;
	private SpeedController rightRearSpeedCtrl = null;
	
	
	private double driveSpeedFactor = 1.0;  //variable to reduce max speed
	public double getDriveSpeedFactor() {
		return driveSpeedFactor;
	}
	
	private double turnSpeedFactor = 1.0; //variable to reduce max turn rate
	public double getTurnSpeedFactor(){
		return turnSpeedFactor;
	}	
	
	
	
	
	//****************************************
	// Encoders
	//****************************************
	private int encoderLeftFrontPortA = 5;
	private int encoderLeftFrontPortB = 6;
	private int encoderRightFrontPortA = 7;
	private int encoderRightFrontPortB = 8;
	private int encoderLeftRearPortA = 9;
	private int encoderLeftRearPortB = 10;
	private int encoderRightRearPortA = 11;
	private int encoderRightRearPortB = 12;
	
	
	private boolean encoderLeftFrontInvert = false;
	private boolean encoderRightFrontInvert = false;
	private boolean encoderLeftRearInvert = false;
	private boolean encoderRightRearInvert = false;
	
	
	private Encoder encoderLeftFront = null;
	public Encoder getEncoderLeftFront(){
		return encoderLeftFront;
	}
	
	private Encoder encoderRightFront = null;
	public Encoder getEncoderRightFront(){
		return encoderRightFront;
	}
	
	private Encoder encoderLeftRear = null;
	public Encoder getEncoderLeftRear(){
		return encoderLeftRear;
	}
	
	private Encoder encoderRightRear = null;
	public Encoder getEncoderRightREar(){
		return encoderRightRear;
	}
		
	
	
	public void loadPrefs(){
		Config c = new Config();
		
		encoderLeftFrontPortA = c.load(prefPrefix + "encoderLeftFrontPortA", encoderLeftFrontPortA);
		encoderLeftFrontPortB = c.load(prefPrefix + "encoderLeftFrontPortB", encoderLeftFrontPortB);
		encoderRightFrontPortA = c.load(prefPrefix + "encoderRightFrontPortA", encoderRightFrontPortA);
		encoderRightFrontPortB = c.load(prefPrefix + "encoderRightFrontPortB", encoderRightFrontPortB);
		encoderLeftRearPortA = c.load(prefPrefix + "encoderLeftRearPortA", encoderLeftRearPortA);
		encoderLeftRearPortB = c.load(prefPrefix + "encoderLeftRearPortB", encoderLeftRearPortB);
		encoderRightRearPortA = c.load(prefPrefix + "encoderRightRearPortA", encoderRightRearPortA);
		encoderRightRearPortB = c.load(prefPrefix + "encoderRightRearPortB", encoderRightRearPortB);
		
		encoderLeftFrontInvert = c.load(prefPrefix + "encoderLeftFrontInvert", encoderLeftFrontInvert);
		encoderRightFrontInvert = c.load(prefPrefix + "encoderRightFrontInvert", encoderLeftFrontInvert);
		encoderLeftRearInvert = c.load(prefPrefix + "encoderLeftRearInvert", encoderLeftRearInvert);
		encoderRightRearInvert = c.load(prefPrefix + "encoderRightRearInvert", encoderRightRearInvert);
		
		
		leftFrontSpeedCtrlPort = c.load(prefPrefix + "leftFrontSpeedCtrlPort", leftFrontSpeedCtrlPort);
		leftRearSpeedCtrlPort = c.load(prefPrefix + "leftRearSpeedCtrlPort", leftRearSpeedCtrlPort);
		rightFrontSpeedCtrlPort = c.load(prefPrefix + "rightFrontSpeedCtrlPort", rightFrontSpeedCtrlPort);
		rightRearSpeedCtrlPort = c.load(prefPrefix + "rightRearSpeedCtrlPort", rightRearSpeedCtrlPort);
		
		invertLeftFront = c.load(prefPrefix + "invertLeftFront", invertLeftFront);
		invertRightFront = c.load(prefPrefix + "invertRightFront", invertRightFront);
		invertLeftRear = c.load(prefPrefix + "invertLeftRear", invertLeftRear);
		invertRightRear = c.load(prefPrefix + "invertRightRear", invertRightRear);
		
		driveSpeedFactor = c.load(prefPrefix + "driveSpeedFactor", driveSpeedFactor);
		turnSpeedFactor = c.load(prefPrefix + "turnSpeedFactor", turnSpeedFactor);
	}

	
	public void init(){
		System.out.println("MD: MechenumDrive Init Starting");
		
		loadPrefs();
		System.out.println("MD: Preferences loaded");
		
    	leftFrontSpeedCtrl = new VictorSP(leftFrontSpeedCtrlPort);
		rightFrontSpeedCtrl = new VictorSP(rightFrontSpeedCtrlPort);
		leftRearSpeedCtrl = new VictorSP(leftRearSpeedCtrlPort);
		rightRearSpeedCtrl = new VictorSP(rightRearSpeedCtrlPort);
		
		roboDrive = new RobotDrive(leftFrontSpeedCtrl, leftRearSpeedCtrl, rightFrontSpeedCtrl, rightRearSpeedCtrl);
		roboDrive.setSafetyEnabled(false);
		//roboDrive.setExpiration(0.5);

		roboDrive.setInvertedMotor(MotorType.kFrontLeft, invertLeftFront);
		roboDrive.setInvertedMotor(MotorType.kFrontRight, invertRightFront);
		roboDrive.setInvertedMotor(MotorType.kRearLeft, invertLeftRear);
		roboDrive.setInvertedMotor(MotorType.kRearRight, invertRightRear);
		

		encoderLeftFront = new Encoder(5,6);
		encoderRightFront = new Encoder(7,8);
		encoderLeftRear = new Encoder(9,10);
		encoderRightRear = new Encoder(11,12);
		
		encoderLeftFront.setReverseDirection(encoderLeftFrontInvert);
		encoderRightFront.setReverseDirection(encoderRightFrontInvert);
		encoderLeftRear.setReverseDirection(encoderLeftRearInvert);
		encoderRightRear.setReverseDirection(encoderRightRearInvert);
		
		
		System.out.println("MD: MechenumDrive Init Complete");	
	}
	
		
    public void initDefaultCommand() {
    	setDefaultCommand(new org.usfirst.frc.team279.robot.commands.MecDriveTeleopDefaultFPS());
    	System.out.println("MD: Set default command to MecDriveTeleopDefaultFPS");
    }
    
    
    public void stop(){
    	roboDrive.stopMotor();
    }
    
}

