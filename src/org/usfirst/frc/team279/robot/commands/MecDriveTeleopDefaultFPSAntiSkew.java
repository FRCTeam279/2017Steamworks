package org.usfirst.frc.team279.robot.commands;



import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.util.NavHelper;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;




/**
 *
 */
public class MecDriveTeleopDefaultFPSAntiSkew extends Command implements PIDOutput {
	private boolean debug = false;
	
	private RobotDrive roboDrive;
	private AHRS ahrs = null;
	
	private Joystick leftJoystick;
	private Joystick rightJoystick;
	
	private final double maxStickMagnitude = java.lang.Math.sqrt(2);
	
	//Anti-Skew PID Variables
	public PIDController pidController;
	private double skewP = 0.006;
	private double skewTolerance = 1.5;
	
	//hold the pidOutput value returned by pidController
	private double pidOutputValue = 0.0;
	
	//this will be updated on the fly in execute so that we know when to setPoint on the PID controller
	// will be false when driver is giving a rotation value
	// when driver transitions from giving rotation to not, we will set this to try and do a setPoint
	// when driver starts giving a rotation again, set back to false
	private boolean wasTurning = false;
	
	//if x and y are 0, then we are at rest, and should not use the correct skew
	// when we come out of rest, setPoint
	// when we go into rest, disable PID
	private boolean wasTranslating = false;
	
	
	
	
	
	//--------------------------------------------------------------------------
    public MecDriveTeleopDefaultFPSAntiSkew() {
    	super("MecDriveTeleopDefaultFPSAntiSkew");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
    }
    
    
    
    
    //--------------------------------------------------------------------------
    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("CMD: MecDriveTeleopDefaultFPSAntiSkew Init Starting");
    	leftJoystick = Robot.oi.getLeftDriverStick();
    	rightJoystick = Robot.oi.getRightDriverStick();
    	roboDrive = Robot.mecanumDrive.getRoboDrive();
    	
    	ahrs = Robot.getAhrs();
    	System.out.println("CMD: MecDriveTeleopDefaultFPSAntiSkew Init Complete");
    	
    	pidController = new PIDController(this.skewP, 0.0, 0.0, 0.0, Robot.getAhrs(), this);
    	pidController.setInputRange(-180, 180);
    	pidController.setOutputRange(-1.0, 1.0);
    	pidController.setAbsoluteTolerance(skewTolerance);
    	pidController.setContinuous(true);
    }

    
    
    //--------------------------------------------------------------------------
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	boolean correctSkew = false;
    	
    	if(leftJoystick == null || rightJoystick == null) {
    		return;
    	}
    	
    	double y = leftJoystick.getY();
    	double x = leftJoystick.getX();
    	double z = rightJoystick.getX();
    	
    	x = Robot.oi.filterInput(x, Robot.oi.getLeftStickNullZone(), 1);
    	y = Robot.oi.filterInput(y, Robot.oi.getLeftStickNullZone(), 1);
    	z = Robot.oi.filterInput(z, Robot.oi.getRightStickNullZone(), 1);
    	
    	boolean translating = false;
    	if(x > 0.0 || x < 0.0 || y > 0.0 || y < 0.0){
    		translating = true;
    	} else {
    		translating = false;
    	}
    	
    	boolean turning = false;
    	if(z > 0.0 || z < 0.0) {
    		turning = true;
    	} else {
    		turning = false;
    	}
    	
    	
    	
    	if (wasTurning && !turning){
    		//System.out.println("CMD: MecDriveTeleopDefaultFPSAntiSkew SetPoint() Called");
    		pidController.setSetpoint(Robot.getAhrs().getYaw());
    	}
    	if(translating && !wasTranslating){
    		//System.out.println("CMD: MecDriveTeleopDefaultFPSAntiSkew SetPoint() Called");
    		pidController.setSetpoint(Robot.getAhrs().getYaw());
    	} 
    	
    	
    	if(translating && !turning) {
    		correctSkew = true;
    		if(!pidController.isEnabled()) {
    			pidController.enable();
    		}
    	} else {
    		correctSkew = false;
    		if(pidController.isEnabled()) {
    			pidController.disable();
    		}
    	}
    	
    	
    	// mecanum turns faster at slow speed than fast speed.. 
    	//	for precision, our concern is the opposite of how we scale with a tank drive
    	//  we need to scale less the higher our magnitude is

    	//SmartDashboard.putNumber("Drive Joystick Magnitude", leftJoystick.getMagnitude());    	
    	//SmartDashboard.putNumber("Drive Joystick Dir Degrees", leftJoystick.getDirectionDegrees()); 
    	double magnitude = java.lang.Math.sqrt((x * x) + (y * y));
    	//SmartDashboard.putNumber("Drive Magnitude", magnitude);	

    	//https://www.desmos.com/calculator/qykgtdk95i
    	
    	// mecanum turns faster at slow speed than fast speed.. 
    	//	for precision, our concern is the opposite of how we scale with a tank drive
    	//  we need to scale the turning less the higher our magnitude is
    	// with no scaling, turning isn't fast enough at full magnitude, so at full magnitude, we want to tone down the magnitude a bit and up the turn to balance it
    	
    	double scaledTurn;
    	if(correctSkew){
    		scaledTurn = pidOutputValue;
    	} else {
    		scaledTurn = z - ((maxStickMagnitude - Math.abs(magnitude) / maxStickMagnitude) * Robot.mecanumDrive.getRotationReductionFactor() * z);
    		//where rotationFactor is the maximum ammount to reduce turn by at zero throttle, 
        	//	eg 0.2 (allowing 80% of rotation speed turning in place)
    	}
    	 	
    	double scaledMagnitude = magnitude - (magnitude * z * Robot.mecanumDrive.getMagnitudeVSTurnFactor());
    	//where magnitudeTurnBorrowFactror is the max amount to reduce Magnitude to allow better turning at full speed
    	
    	if(debug){
	    	SmartDashboard.putNumber("Drive z", z);
	    	SmartDashboard.putNumber("Drive scaledTurn", scaledTurn);
	    	SmartDashboard.putNumber("Drive Scaled Magnitude",  scaledMagnitude);
	    	SmartDashboard.putBoolean("Drive correctSkew", correctSkew);
	    	SmartDashboard.putBoolean("Drive wasTranslating", wasTranslating);
    	}
    	
    	//get magnitude and direction of skewing that is not in the desired direction
    	//calculate reciprocal
    	//apply
    	
    	
    	//Robot oriented
    	//roboDrive.mecanumDrive_Polar(scaledMagnitude, leftJoystick.getDirectionDegrees(), scaledTurn);
    	
    	//Field Oriented
    	roboDrive.mecanumDrive_Polar(scaledMagnitude, NavHelper.addDegrees(leftJoystick.getDirectionDegrees(), -1 * ahrs.getAngle()), scaledTurn);
    	
    	wasTranslating = translating;
    	wasTurning = turning;
    	
    }

    
    
    
    //--------------------------------------------------------------------------
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	wasTurning = false;
    	wasTranslating = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	wasTurning = false;
    	wasTranslating = false;
    }
    
    
    public void pidWrite(double output) {
    	pidOutputValue = output;
    }
    
    
    
    
}
