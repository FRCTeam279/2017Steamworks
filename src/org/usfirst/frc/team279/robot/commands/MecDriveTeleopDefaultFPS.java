package org.usfirst.frc.team279.robot.commands;



import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.util.NavHelper;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Joystick;




/**
 *
 */
public class MecDriveTeleopDefaultFPS extends Command {
	private RobotDrive roboDrive;
	private AHRS ahrs = null;
	
	private Joystick leftJoystick;
	private Joystick rightJoystick;
	
	private final double maxStickMagnitude = java.lang.Math.sqrt(2);
	
	
	
	//--------------------------------------------------------------------------
    public MecDriveTeleopDefaultFPS() {
    	super("MecDriveTeleopDefaultFPS");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
    }
    
    
    
    
    //--------------------------------------------------------------------------
    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("CMD: MecDriveTeleopDefaultFPS Init Starting");
    	leftJoystick = Robot.oi.getLeftDriverStick();
    	rightJoystick = Robot.oi.getRightDriverStick();
    	roboDrive = Robot.mecanumDrive.getRoboDrive();
    	
    	ahrs = Robot.getAhrs();
    	System.out.println("CMD: MecDriveTeleopDefaultFPS Init Complete");
    }

    
    
    //--------------------------------------------------------------------------
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(leftJoystick == null || rightJoystick == null) {
    		return;
    	}
    	
    	double y = leftJoystick.getY();
    	double x = leftJoystick.getX();
    	double z = rightJoystick.getX();
    	
    	if(Robot.oi.slowSpeedBtn.get()) {
    		y = y/2;
    		x = x/2;
    	}
    	
    	x = Robot.oi.filterInput(x, Robot.oi.getLeftStickNullZone(), 1);
    	y = Robot.oi.filterInput(y, Robot.oi.getLeftStickNullZone(), 1);
    	z = Robot.oi.filterInput(z, Robot.oi.getRightStickNullZone(), 1);
    	
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
    	
    	
    	double scaledTurn = z - ((maxStickMagnitude - Math.abs(magnitude) / maxStickMagnitude) * Robot.mecanumDrive.getRotationReductionFactor() * z);
    	//where rotationFactor is the maximum ammount to reduce turn by at zero throttle, 
    	//	eg 0.2 (allowing 80% of rotation speed turning in place)
    	
    	
    	double scaledMagnitude = magnitude - (magnitude * z * Robot.mecanumDrive.getMagnitudeVSTurnFactor());
    	//where magnitudeTurnBorrowFactror is the max amount to reduce Magnitude to allow better turning at full speed
    	
    	//SmartDashboard.putNumber("Drive z", z);
    	//SmartDashboard.putNumber("Drive scaledTurn", scaledTurn);
    	//SmartDashboard.putNumber("Drive Scaled Magnitude",  scaledMagnitude);
    	
    	
    	//get magnitude and direction of scewing that is not in the desired direction
    	//calculate reciprocal
    	//apply
    	
    	
    	//Robot oriented
    	//roboDrive.mecanumDrive_Polar(scaledMagnitude, leftJoystick.getDirectionDegrees(), scaledTurn);
    	
    	//Field Oriented
    	roboDrive.mecanumDrive_Polar(scaledMagnitude, NavHelper.addDegrees(leftJoystick.getDirectionDegrees(), -1 * ahrs.getAngle()), scaledTurn);
    }

    
    
    
    //--------------------------------------------------------------------------
    
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
