package org.usfirst.frc.team279.robot.commands;



import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team279.robot.Robot;

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
    	
    	x = Robot.oi.filterInput(x, Robot.oi.getLeftStickNullZone(), 1);
    	y = Robot.oi.filterInput(y, Robot.oi.getLeftStickNullZone(), 1);
    	z = Robot.oi.filterInput(z, Robot.oi.getRightStickNullZone(), 1);
    	
    	//this needs to have configurable parameters
    	//boolean halfSpeed = Robot.oi.getLeftDriverStick().getTrigger();  
    	//if(halfSpeed) {
    	//	x *= 0.5;
    	//	y *= 0.5;
    	//}
    	
    	// mecanum turns faster at slow speed than fast speed.. 
    	//	for precision, our concern is the opposite of how we scale with a tank drive
    	//  we need to scale less the higher our magnitude is

    	double magnitude = java.lang.Math.sqrt((x * x) + (y * y));
    	double scaledTurn = z * (((maxStickMagnitude - magnitude) / maxStickMagnitude) * Robot.mecanumDrive.getTurnSpeedFactor());
    	//SmartDashboard.putNumber("Drive Scaled Turn", scaledTurn);
    	//SmartDashboard.putNumber("Drive z", z);
    	
    	//determine how to switch to robot oriented later
    	//if(false) {
    	//	roboDrive.mecanumDrive_Polar(magnitude, leftJoystick.getDirectionDegrees(), z);
    	//} else {
    		//field oriented
    	//System.out.println("DriveDefault: xyz: " + x + ", " + y + ", " + z);
    	
    	
    	//http://www.pdocs.kauailabs.com/navx-mxp/examples/field-oriented-drive/
    	roboDrive.mecanumDrive_Cartesian(x, y, scaledTurn, ahrs.getAngle());
    	//}
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
