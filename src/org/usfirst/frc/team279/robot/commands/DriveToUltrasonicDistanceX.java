package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveToUltrasonicDistanceX extends Command implements PIDOutput, PIDSource {
	private boolean useSmartDashBoardValues = false;
	
	private String[] ultrasonicKeys;

	public PIDController pidController;
    private double target = 0.0;
    
    private double kP = 0.000;
    private double kI = 0.000;
    private double kD = 0.000;
    private double kF = 0.000;
    private double kTolerance = 0.0;
    private double minSpeed = 0.0;
    private double maxSpeed = 1.0;
    private double minInput = 0.0;
    private double maxInput = 300;
    
    private boolean cancelling = false;
    
    public DriveToUltrasonicDistanceX(String ultrasonicName) {
    	super("DriveToUltrasonicDistanceX");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        this.ultrasonicKeys = new String[1];
        this.ultrasonicKeys[0] = ultrasonicName;
        Robot.ultrasonics.getUltrasonics().enableUltrasonic(ultrasonicName);
        useSmartDashBoardValues = true;
    }
    
    public DriveToUltrasonicDistanceX(String[] ultrasonicNames) {
    	super("DriveToUltrasonicDistanceX");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        this.ultrasonicKeys = new String[1];
        this.ultrasonicKeys = ultrasonicNames;
        for(String key : ultrasonicNames) {
        	Robot.ultrasonics.getUltrasonics().enableUltrasonic(key);
        }
        
        useSmartDashBoardValues = true;
    }
    
    public DriveToUltrasonicDistanceX(String ultrasonicName, double target, double p, double i, double d, double tolerance) {
    	super("DriveToUltrasonicDistanceX");
        requires(Robot.mecanumDrive);
        
        this.ultrasonicKeys = new String[1];
        this.ultrasonicKeys[0] = ultrasonicName;
        Robot.ultrasonics.getUltrasonics().enableUltrasonic(ultrasonicName);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;
        this.target = target;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kTolerance = tolerance;        
    }
    
    public DriveToUltrasonicDistanceX(String ultrasonicName, double target, double p, double i, double d, double tolerance, double minSpeed) {
    	super("DriveToUltrasonicDistanceX");
        requires(Robot.mecanumDrive);
        
        this.ultrasonicKeys = new String[1];
        this.ultrasonicKeys[0] = ultrasonicName;
        Robot.ultrasonics.getUltrasonics().enableUltrasonic(ultrasonicName);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;
        this.target = target;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kTolerance = tolerance;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.minInput = minInput;
        this.maxInput = maxInput;
        
    }
    
    //check multiple and use the lowest value
    public DriveToUltrasonicDistanceX(String[] ultrasonicNames, double target, double p, double i, double d, double tolerance, double minSpeed) {
    	super("DriveToUltrasonicDistanceX");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;
        this.ultrasonicKeys = ultrasonicNames;
        for(String key : ultrasonicNames) {
        	Robot.ultrasonics.getUltrasonics().enableUltrasonic(key);
        }
        this.target = target;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kTolerance = tolerance;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.minInput = minInput;
        this.maxInput = maxInput;
        
    }
    
    //be sure the ultrasonics are enabled prior to getting to this point... 
    // attempting to enable them in the command will likely lead to sporadic behavior
    protected void initialize() {   
    	if(useSmartDashBoardValues) {
	    	target = SmartDashboard.getNumber("USDD Target", 24.0);
			kP = SmartDashboard.getNumber("USDD P", 0.01);
			kI = SmartDashboard.getNumber("USDD I", 0.00);
			kD = SmartDashboard.getNumber("USDD D", 0.0);
			minSpeed = SmartDashboard.getNumber("USDD MinSpeed", 0.0);
			maxSpeed = SmartDashboard.getNumber("USDD MaxSpeed", 1.0);
			kTolerance = SmartDashboard.getNumber("USDD Tolerance", 12);
    	}
    	
    	pidController = new PIDController(kP, kI, kD, kF, this, this);
    	pidController.setInputRange(minInput, maxInput);
    	pidController.setOutputRange(maxSpeed * -1.0, maxSpeed);
    	pidController.setAbsoluteTolerance(kTolerance);
    	pidController.setContinuous(false);
        pidController.setSetpoint(target);
        
        System.out.println("CMD USDD: Starting - target: " + this.target + ", current: " + getShortest());
    }

   
    protected void execute() {
    	if(pidController == null && !this.cancelling) {
    		this.initialize();
    	}
    	if(!pidController.isEnabled() && !this.cancelling){
    		pidController.enable();
    	}
    }

    
    protected boolean isFinished() {
    	if(this.cancelling){ return true; }
    	return pidController.onTarget();
    }

    
    protected void end() {
    	System.out.println("CMD USDD: Ended - target: " + this.target + ", current: " + getShortest());
    	Robot.mecanumDrive.stop();
    	pidController.disable();
    	pidController = null;
    	this.cancelling = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	System.out.println("CMD USDD: Interrupted - target: " + this.target + ", current: " + getShortest());
    	Robot.mecanumDrive.stop();
    	pidController.disable();
    	pidController = null;
    	this.cancelling = false;
    }
    
    
    public PIDSourceType getPIDSourceType() {
    	return PIDSourceType.kDisplacement;
    }
    
    public void setPIDSourceType(PIDSourceType pidSource) {
    	
    }
    
    
    private double getShortest(){
    	double shortest = 10000;
    	double temp = 0.0;
    	 for(String key : ultrasonicKeys) {
         	temp = Robot.ultrasonics.getUltrasonics().getDistanceInches(key);
         	if(temp < shortest) {
         		shortest = temp;
         	}
        }
    	return shortest;
    }
    
    
    public double pidGet(){
    	double distance = -1.0;
    	int countInvalid = 0;
    	
    	while(true) {
    		distance = getShortest();
    		if(distance < 0.0 || distance > 250){
        		countInvalid++;
        		if(countInvalid == 3) {
            		System.out.println("CMD USDD: Warning! Three invalid measurements received.. ending command");
            		this.cancelling = true;
            		return this.target;
            	} 
        	} else {
        		return distance;
        	}
    	}
    }
    
    public void pidWrite(double output) {
    	output = output * -1.0;
    	if(this.cancelling){ 
    		Robot.mecanumDrive.stop(); 
    	} else {
    		if(Math.abs(output) < this.minSpeed) {
    			//System.out.println("CMD USDD: MinSpeed Reached (Output: " + output + ")");
    			if(output > 0.0) {
    				Robot.mecanumDrive.getRoboDrive().mecanumDrive_Cartesian(minSpeed, 0.0, 0.0, 0.0);
    			} else {
    				Robot.mecanumDrive.getRoboDrive().mecanumDrive_Cartesian(minSpeed * -1.0, 0.0, 0.0, 0.0);
    			}
    		} else {
    			Robot.mecanumDrive.getRoboDrive().mecanumDrive_Cartesian(output, 0.0, 0.0, 0.0);
    		}
    	}
    }
}
