package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.util.NavHelper;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class YawPID extends Command implements PIDOutput {
	private boolean useSmartDashBoardValues = false;

	public PIDController pidController;
    private double yaw = 0.0;
    private double targetHeading = 0.0;
    private double kP = 0.000;
    private double kI = 0.000;
    private double kD = 0.000;
    private double kF = 0.000;
    private double kTolerance = 0.0;
    private double minSpeed = -1.0;
    private double maxSpeed = 1.0;
    private boolean useRelativeYaw = true;
    private boolean cancel = false;
    
    //dir is +/- 180
    public YawPID() {
    	super("YawPID");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);

        useSmartDashBoardValues = true;
    }
    
    
    public YawPID(double yaw, double p, double i, double d, double tolerance) {
    	super("YawPID");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;

        this.yaw = NavHelper.addDegreesYaw(yaw, 0); //ensure ti's between -180 and 180;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kTolerance = Math.abs(tolerance);        
    }
    
    public YawPID(double yaw, double p, double i, double d, double tolerance, double minSpeed) {
    	super("YawPID");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;
        this.yaw = NavHelper.addDegreesYaw(yaw, 0); //ensure ti's between -180 and 180;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kTolerance = tolerance;
        this.minSpeed = minSpeed; 
    }
    
    
    //useRelativeYaw = yaw is relative to current heading rather than absolute value
    public YawPID(double yaw, double p, double i, double d, double tolerance, double minSpeed, boolean useRelativeYaw) {
    	super("YawPID");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;
        this.yaw = NavHelper.addDegreesYaw(yaw, 0); //ensure ti's between -180 and 180;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kTolerance = tolerance;
        this.minSpeed = minSpeed; 
        this.useRelativeYaw = useRelativeYaw;
    }
    
    
    //be sure the ultrasonics are enabled prior to getting to this point... 
    // attempting to enable them in the command will likely lead to sporadic behavior
    protected void initialize() {
    	this.cancel = false;
    	
    	if(useSmartDashBoardValues) {
	    	yaw = NavHelper.addDegreesYaw(SmartDashboard.getNumber("TurnPID Target", 0.0), 0.0);
			kP = SmartDashboard.getNumber("TurnPID P", 0.01	);
			kI = SmartDashboard.getNumber("TurnPID I", 0.00);
			kD = SmartDashboard.getNumber("TurnPID D", 0.0);
			minSpeed = SmartDashboard.getNumber("TurnPID MinSpeed", 0.0);
			kTolerance = Math.abs(SmartDashboard.getNumber("TurnPID Tolerance", 10));
    	}
    	
    	
    	//the AHRS pidGet() method returns a yaw (+- 180)
    	if(useRelativeYaw) {
    		targetHeading = NavHelper.addDegreesYaw(yaw, Robot.getAhrs().getYaw());
    	} else {
    		targetHeading = yaw;
    	}
    	pidController = new PIDController(kP, kI, kD, kF, Robot.getAhrs(), this);
    	pidController.setInputRange(-180.0f, 180.0f);
    	pidController.setOutputRange(-1.0, 1.0);
    	pidController.setContinuous(true);
    	pidController.setAbsoluteTolerance(kTolerance);
        pidController.setSetpoint(targetHeading);
        if(this.useRelativeYaw){
        	System.out.println("CMD YawPID: Starting - yaw (relative): " + yaw + ", target: " + targetHeading + ", current: " + Robot.getAhrs().pidGet());
        } else {
        	System.out.println("CMD YawPID: Starting - yaw (absolute): " + yaw + ", target: " + targetHeading + ", current: " + Robot.getAhrs().pidGet());
        }
    }

   
    protected void execute() {
    	if(this.cancel) { return; }
    	
    	if(pidController == null) {
    		this.initialize();
    	}
    	if(!pidController.isEnabled()){
    		pidController.enable();
    	}
    }

    
    protected boolean isFinished() {
    	return pidController.onTarget();
    }

    
    protected void end() {
    	System.out.println("CMD YawPID: Ended - target: " + targetHeading + ", current: " + Robot.getAhrs().pidGet());
    	Robot.mecanumDrive.stop();
    	pidController.disable();
    	pidController = null;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	System.out.println("CMD YawPID: Interrupted - target: " + targetHeading + ", current: " + Robot.getAhrs().pidGet());
    	Robot.mecanumDrive.stop();
    	pidController.disable();
    	pidController = null;
    }
    
    
    
    //remember that -Y is forwards
    public void pidWrite(double output) {
    
    	if(Math.abs(output) < this.minSpeed) {
			double tSpd = minSpeed;
			if(output < 0.0) {
				tSpd = minSpeed * -1.0;
			} 
			//System.out.println("CMD YawPID: current: " + Robot.getAhrs().pidGet() + ", Output=" +  output + " minspeed=" + tSpd);
			Robot.mecanumDrive.getRoboDrive().mecanumDrive_Cartesian(0.0, 0.0, tSpd, 0.0);
			
		} else {
			//System.out.println("CMD YawPID: current: " + Robot.getAhrs().pidGet() + ",  Output=" +  output);
			Robot.mecanumDrive.getRoboDrive().mecanumDrive_Cartesian(0.0, 0.0, output, 0.0);
		}
    }
}
