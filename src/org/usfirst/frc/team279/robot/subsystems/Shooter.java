package org.usfirst.frc.team279.robot.subsystems;

import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.util.Config;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {
	
	/*
	 * Note:
	 * > Encoder is between the motor and the gearbox (measures direct motor speeds)
	 * > Angle will now be two fixed angles which are TBD
	 * > 
	 */
	
	//The preferencesPrefix will be prepended to the preferences loaded from the Robot Preferences
	private String prefPrefix = "sh_";
	
	//Shooter motor max speed
	private double  maxShooterMotorRPM = 18730.0;//RPM
	
	//Angles - in degrees
	private double  degOne    = 0.0;
	private double  degTwo    = 0.0;
	private boolean whichDeg = false; //One = false; Two = true;
	
	//MAX and MIN values at degOne and degTwo
	private double  degOneDistanceMax = 0.0;
	private double  degOneDistanceMin = 0.0;
	private double  degOneSpeedMax    = 0.0;
	private double  degOneSpeedMin    = 0.0;
	private double  degTwoDistanceMax = 0.0;
	private double  degTwoDistanceMin = 0.0;
	private double  degTwoSpeedMax    = 0.0;
	private double  degTwoSpeedMin    = 0.0;
	
	//Shooter PID Values
	private double  p             = 0.025;
	private double  i             = 0.0;
	private double  d             = 0.00025;
	private double  f             = 0.085;
	private double  dP            = 0.025;
	private double  dI            = 0.0;
	private double  dD            = 0.00025;
	private double  dF            = 0.0085;
	
	//Shooter CANTalon with getter
	private CANTalon shooterMotor = null;
	/**
	 * @return Shooter SpeedController
	 * @see CANTalon
	 */
	public CANTalon getShooterController() {
		return shooterMotor;
	}
	
	//SpeedController Ports
	private int     shooterPort   = 1;
	
	//SpeedController Inverts
	private boolean invertShooter = false;
	
	//CANTalon Encoder Invert
	private boolean invertEncoder = true;
	
	
	
	//*** INIT *******************************************************
	
    public void init() throws RuntimeException {
    	
    	System.out.println("SH: Shooter Init Starting");
    	
    	loadPrefs();
		System.out.println("SH: Preferences loaded");
    	
    	shooterMotor = new CANTalon(shooterPort);
    	//feedMotor    = new Talon(feedPort);  commented out because the talons are not on the robot
		System.out.println("SH: Speed Controllers Setup");
    	
    	shooterConfig();
		System.out.println("SH: Shooter Config Loaded");
    }

    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	//System.out.println("SH: Set default command to ...");
    }
    
    
    /**
     * Loads the values from the SmartDashboard
     */
    public void loadPrefs() {
		Config c = new Config();
		
		shooterPort   = c.load(prefPrefix + "shooterPort", shooterPort);
		invertShooter = c.load(prefPrefix + "invertShooter", invertShooter);
		invertEncoder = c.load(prefPrefix + "invertEncoder", invertEncoder);
		
		dP            = c.load(prefPrefix + "defaultP", dP);
		dI            = c.load(prefPrefix + "defaultI", dI);
		dD            = c.load(prefPrefix + "defaultD", dD);
		dF            = c.load(prefPrefix + "defaultF", dF);
		
		degOne        = c.load(prefPrefix + "degreeOne", degOne);
		degTwo        = c.load(prefPrefix + "degreeTwo", degTwo);
		whichDeg      = c.load(prefPrefix + "whichDegree", whichDeg);
		
		maxShooterMotorRPM = c.load(prefPrefix + "maxShooterMotorRPM", maxShooterMotorRPM);

		degOneDistanceMax = c.load(prefPrefix + "degOneDistanceMax", degOneDistanceMax);
		degOneDistanceMin = c.load(prefPrefix + "degOneDistanceMin", degOneDistanceMin);
		degOneSpeedMax    = c.load(prefPrefix + "degOneSpeedMax", degOneSpeedMax);
		degOneSpeedMin    = c.load(prefPrefix + "degOneSpeedMin", degOneSpeedMin);
		degTwoDistanceMax = c.load(prefPrefix + "degTwoDistanceMax", degTwoDistanceMax);
		degTwoDistanceMin = c.load(prefPrefix + "degTwoDistanceMin", degTwoDistanceMin);
		degTwoSpeedMax    = c.load(prefPrefix + "degTwoSpeedMax", degTwoSpeedMax);
		degTwoSpeedMin    = c.load(prefPrefix + "degTwoSpeedMin", degTwoSpeedMin);
	}
    
    
    /**
     * Sets up the TalonSRX for the shooter
     */
    private void shooterConfig() {
    	//Setup Feedback
    	shooterMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	shooterMotor.reverseSensor(invertEncoder);
    	
    	//Setup the Voltage Max and Nominal
    	shooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
    	shooterMotor.configPeakOutputVoltage(+12.0f, -12.0f);
    	
    	//Setup PID
    	resetDefaultPIDValues();
    	resetPID();
    	
    	//Other Setup
    	shooterMotor.enableBrakeMode(false);
    	shooterMotor.reverseOutput(invertShooter);
    }    
    
    
    
    //*** SHOOTER MOTOR **********************************************
    
    /**
     * Sets a new default PID Value
     * @param p Proportional
     * @param i Integral
     * @param d Derivative
     * @param f Feed
     */
    public void setDefaultPIDValues(double p, double i, double d, double f) {
    	this.p = p;
    	this.i = i;
    	this.d = d;
    	this.f = f;
    }
    
    
    /**
     * Resets PID default values to the values received from prefs
     */
    public void resetDefaultPIDValues() {
    	p = dP;
    	i = dI;
    	d = dD;
    	f = dF;
    }
    
    
    /**
     * Sets PID to the current PID default values
     */
    private void resetPID() {
    	shooterMotor.setP(p);
    	shooterMotor.setI(i);
    	shooterMotor.setD(d);
    	shooterMotor.setF(f);
    }
    
    
    /**
     * Sets the motor speed according to the inputed RPM
     * Uses default PID settings
     * @param rpm Speed in Revolutions Per Minute
     */
    public void shootRPM(double rpm) {
    	//resetPID();
    	
    	//prevent errors
    	if(rpm > maxShooterMotorRPM) { rpm = maxShooterMotorRPM; }
    	if(rpm < -maxShooterMotorRPM){ rpm = -maxShooterMotorRPM; }
    	
    	//set speed
    	shooterMotor.changeControlMode(TalonControlMode.Speed);
    	shooterMotor.set(rpm);
    }
    
    
    /**
     * Sets the motor speed according to the inputed speed
     * No PID used
     * @param pwm Number between -1 and 1
     */
    public void shootPWM(double pwm) {
    	resetPID();
    	
    	//prevent errors
    	if(pwm>1) { pwm = 1; }
    	if(pwm<-1){ pwm = -1;}
    	
    	//set speed
    	shooterMotor.changeControlMode(TalonControlMode.Voltage);
    	shooterMotor.set(pwm*12); //12 = Max Voltage 
    }
    
    
    /**
     * Sets the motor speed according to the inputed RPM
     * Sets a custom PID for the speed
     * @param rpm Speed in Revolutions Per Minute
     * @param p Proportional
     * @param i Integral
     * @param d Derivative
     * @param f Feed
     */
    public void shootPID(double rpm, double p, double i, double d, double f) {
    	//Setup PID
    	shooterMotor.setP(p);
    	shooterMotor.setI(i);
    	shooterMotor.setD(d);
    	shooterMotor.setF(f);
    	
    	//prevent errors
    	if(rpm>maxShooterMotorRPM) { rpm = maxShooterMotorRPM; }
    	if(rpm<-maxShooterMotorRPM){ rpm = -maxShooterMotorRPM;}
    	
    	//set speed
    	shooterMotor.changeControlMode(TalonControlMode.Speed);
    	shooterMotor.set(rpm);
    }
    
    
    /**
     * Stops the motor running the shooter
     */
    public void stopShooter() {
    	shooterMotor.stopMotor();
    }    
    
    
    
    //*** CALCULATIONS ***********************************************
    
    /**
     * Gets the distance from the camera and compares it to pre-recorded
     * information(Max and Min shot data) to get the speed needed. Also
     * takes in to account the which angle is being used.
     * 
     * {@code percent = d / (dMax - dMin);}
     * {@code speed = (spdMax - spdMin) * %}
     * speed calc veries based on the two angles.
     * 
     * @return RPM needed to make the shot
     */    
    public double calcSpeedFromAngle() {
    	double percent = 0.0;
    	
    	if(!whichDeg) {
    		percent = getDistance() / (degOneDistanceMax - degOneDistanceMin);
    		return (degOneSpeedMax - degOneSpeedMin) * percent;
    	} else {
    		percent = getDistance() / (degTwoDistanceMax - degTwoDistanceMin);
    		return (degTwoSpeedMax - degTwoSpeedMin) * percent;    		
    	}
    }

    
    
    //*** VISION GETTER'S ********************************************
    
    /**
     * Updates the Distance from the camera to the base of the tower 
     * based on the information received from the Rasp PI through the
     * Network Tables
     * 
     * If {@value -1.0} is returned then an error occurred while
     * retrieving the value.
     * 
     * @return Distance from the camera to the target
     */
	public double getDistance() {
		try {
			return Robot.boilerTable.getNumber("distance", 0.0);
		} catch(Exception e) {
    		System.err.println("NetworkTables Error: Failed to get a value for 'distance' from 'boilerTable'");
			return -1.0;
		}
    }
    
    
    /**
     * Updates the Rotation Angle of the robot based on the information
     * received from the Rasp PI through the Network Tables
     * 
     * If {@value -1.0} is returned then an error occurred while
     * retrieving the value.
     * 
     * @return Horz. Angle from the front of the camera to the target
     */
    public double getAngle() {
    	try {
    		return Robot.boilerTable.getNumber("angle", 0.0);
    	} catch(Exception e) {
    		System.err.println("NetworkTables Error: Failed to get a value for 'angle' from 'boilerTable'");
    		return -1.0;
    	}
    }
    
    
    /**
     * Tells you if the camera sees the target
     * @return True if target is seen, False if target is not seen
     */
    public boolean getEyes() {
    	try {
    		return Robot.boilerTable.getBoolean("eyes", false);
    	} catch(Exception e) {
    		System.err.println("NetworkTables Error: Failed to get a value for 'eyes' from 'boilerTable'");
    		return false;
    	}
    }
}

