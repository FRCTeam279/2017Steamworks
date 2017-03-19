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
public class DriveToEncoderDistance extends Command implements PIDOutput, PIDSource {
	private boolean useSmartDashBoardValues = false;
	
	private Encoder enc;
	private double direction;   //+ or - 180
	public PIDController pidController;
    private double target = 0.0;
    
    private double kP = 0.000;
    private double kI = 0.000;
    private double kD = 0.000;
    private double kF = 0.000;
    private double kTolerance = 0.0;
    private double minSpeed = 0.0;
    private double maxSpeed = 1.0;
    private double minInput = -10000;
    private double maxInput = 10000;
    
    private boolean cancel = false;
    
    //dir is +/- 180
    public DriveToEncoderDistance(Encoder encoder) {
    	super("DriveToEncoderDistance");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        
        this.enc = encoder;
        
        useSmartDashBoardValues = true;
    }
    
    
    public DriveToEncoderDistance(Encoder encoder, double dir, double target, double p, double i, double d, double tolerance) {
    	super("DriveToEncoderDistance");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;
        this.enc = encoder;
        this.direction = dir;
        this.target = target;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kTolerance = Math.abs(tolerance);        
    }
    
    public DriveToEncoderDistance(Encoder encoder, double dir, double target, double p, double i, double d, double tolerance, double minSpeed, double maxSpeed, double minInput, double maxInput) {
    	super("DriveToEncoderDistance");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;
        this.enc = encoder;
        this.direction = dir;
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
    	this.cancel = false;
    
    	if(useSmartDashBoardValues) {
    		direction = SmartDashboard.getNumber("DriveEnc Dir", 0.0);
	    	target = SmartDashboard.getNumber("DriveEnc Target", -2000.0);
			kP = SmartDashboard.getNumber("DriveEnc P", 0.005);
			kI = SmartDashboard.getNumber("DriveEnc I", 0.00);
			kD = SmartDashboard.getNumber("DriveEnc D", 0.0);
			minSpeed = SmartDashboard.getNumber("DriveEnc MinSpeed", 0.0);
			maxSpeed = SmartDashboard.getNumber("DriveEnc MaxSpeed", 1.0);
			kTolerance = Math.abs(SmartDashboard.getNumber("DriveEnc Tolerance", 500));
    	} 
    	
    	enc.reset();
    	
    	pidController = new PIDController(kP, kI, kD, kF, this, this);
    	pidController.setInputRange(minInput, maxInput);
    	pidController.setOutputRange(maxSpeed * -1.0, maxSpeed);
    	pidController.setAbsoluteTolerance(kTolerance);
    	pidController.setContinuous(false);
        pidController.setSetpoint(target);
        
        System.out.println("CMD DriveEnc: Starting(" + System.currentTimeMillis() + ") - target: " + this.target + ", current: " + enc.get());
    }

   
    protected void execute() {
    	if(this.cancel){ return; }
    	
    	if(pidController == null) {
    		this.initialize();
    	}
    	if(!pidController.isEnabled()){
    		pidController.enable();
    	}
    }

    
    protected boolean isFinished() {
    	if(this.cancel){ return true; }
    	return Math.abs(target - enc.get()) < kTolerance;
    }

    
    protected void end() {
    	System.out.println("CMD DriveEnc: Ended(" + System.currentTimeMillis() + ") - target: " + this.target + ", current: " + enc.get());
    	Robot.mecanumDrive.stop();
    	pidController.disable();
    	pidController = null;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	System.out.println("CMD DriveEnc: Interrupted(" + System.currentTimeMillis() + ") - target: " + this.target + ", current: " + enc.get());
    	Robot.mecanumDrive.stop();
    	pidController.disable();
    	pidController = null;
    }
    
    
    public PIDSourceType getPIDSourceType() {
    	return PIDSourceType.kDisplacement;
    }
    
    public void setPIDSourceType(PIDSourceType pidSource) {
    	
    }
    
    public double pidGet(){
    	return enc.get();
    }
    
    
    //remember that -Y is forwards
    public void pidWrite(double output) {
    	if(this.pidController ==  null) { 
    		System.out.println("CMD DriveEnc: Warning! pidWrite Called after pidController set to null!");
    		Robot.mecanumDrive.stop();
    		return;
    	}
    	
    	if(this.cancel || this.isFinished()){ 
    		Robot.mecanumDrive.stop(); 
    	} else {
    		if(Math.abs(output) < this.minSpeed) {
    			//System.out.println("CMD USDD: MinSpeed Reached (Output: " + output + ")");
    			if(output > 0.0) {
    				Robot.mecanumDrive.getRoboDrive().mecanumDrive_Cartesian(0.0, minSpeed, 0.0, direction);
    			} else {
    				Robot.mecanumDrive.getRoboDrive().mecanumDrive_Cartesian(0.0, minSpeed * -1.0, 0.0, direction);
    			}
    		} else {
    			Robot.mecanumDrive.getRoboDrive().mecanumDrive_Cartesian(0.0, output, 0.0, direction);
    		}
    	}
    }
}
