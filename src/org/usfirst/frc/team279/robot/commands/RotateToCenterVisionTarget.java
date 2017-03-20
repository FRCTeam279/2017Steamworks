package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.util.NavHelper;
import org.usfirst.frc.team279.util.SamplesSystem;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateToCenterVisionTarget extends Command implements PIDOutput {
	private boolean useSmartDashBoardValues = false;

	
	private NetworkTable nt;
	public PIDController pidController;
	private String table;
	private String key;
	private double yaw = 0.0;
    private double targetHeading = 0.0;
    private double kP = 0.000;
    private double kI = 0.000;
    private double kD = 0.000;
    private double kF = 0.000;
    private double kTolerance = 0.0;
    private double minSpeed = -1.0;
    private double maxSpeed = 1.0;

    private double imageWidth = 320.0;
    
    private boolean cancel = false;
    private int errorCount = 0;
    
    private SamplesSystem samples = new SamplesSystem();
    private int sampleCount = 0;
    
    public RotateToCenterVisionTarget(String table, String key) {
    	super("RotateToCenterVisionTarget");
        requires(Robot.mecanumDrive);
        this.table = table;
        this.key = key;
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);

        useSmartDashBoardValues = true;
    }
    
    
    public RotateToCenterVisionTarget(String table, String key, double p, double i, double d, double tolerance) {
    	super("RotateToCenterVisionTarget");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;
        this.table = table;
        this.key = key;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kTolerance = Math.abs(tolerance);        
    }
    
    public RotateToCenterVisionTarget(String table, String key, double p, double i, double d, double tolerance, double minSpeed) {
    	super("RotateToCenterVisionTarget");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;
        this.table = table;
        this.key = key;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kTolerance = tolerance;
        this.minSpeed = minSpeed; 
    }
    
    //be sure the ultrasonics are enabled prior to getting to this point... 
    // attempting to enable them in the command will likely lead to sporadic behavior
    protected void initialize() {
    	this.cancel = false;
    	nt = NetworkTable.getTable(table);
    	
    	if(useSmartDashBoardValues) {
			kP = SmartDashboard.getNumber("TurnPID P", 0.002);
			kI = SmartDashboard.getNumber("TurnPID I", 0.00);
			kD = SmartDashboard.getNumber("TurnPID D", 0.0);
			minSpeed = SmartDashboard.getNumber("TurnPID MinSpeed", 0.18);
			kTolerance = Math.abs(SmartDashboard.getNumber("TurnPID Tolerance", 20.0));
    	} 

    	
    	
    	pidController = new PIDController(kP, kI, kD, kF, Robot.getAhrs(), this);
    	pidController.setInputRange(-180.0f, 180.0f);
    	pidController.setOutputRange(-1.0, 1.0);
    	pidController.setContinuous(true);
    	pidController.setAbsoluteTolerance(kTolerance);
    	System.out.println("CMD RotateToCenterVisionTarget: Starting");

        
        
    }

   
    protected void execute() {
    	if(this.cancel) { return; }
    	if(pidController == null) {
    		this.initialize();
    	}
    	
    	if(!pidController.isEnabled()) {
	    	if(sampleCount < 10) {
	    		sampleCount += 1;
		    	if(!nt.getBoolean("eyes", false)){
		    		errorCount += 1;
		    		if(errorCount > 10){
		    			System.out.println("CMD RotateToCenterVisionTarget: Warning! No eyes on target.. ending command");
		    			this.cancel = true;
		    			return;
		    		}
		    	} else {
		    		errorCount = 0;
		    		samples.setSample(nt.getNumber(key, 0.0));
		    	}
	    	} else {
	    		double angle = samples.getAverage();
	    		System.out.println("CMD RotateToCenterVisionTarget: angle avg = " + angle);
	    		
	    		// setpoint to the inverse of the offset, and add to current yaw
	    		targetHeading = NavHelper.addDegreesYaw((angle * -1.0), Robot.getAhrs().getYaw());
	    		pidController.setSetpoint(targetHeading);
	    		pidController.enable();
	    		System.out.println("CMD RotateToCenterVisionTarget: Starting - angle: " + angle + ", targetHeading: " + targetHeading + ", current: " + Robot.getAhrs().pidGet());
	    	}
    	}
        
    }

    
    protected boolean isFinished() {
    	if(this.cancel){ return true; }
    	return pidController.onTarget();
    }

    
    protected void end() {
    	System.out.println("CMD RotateToCenterVisionTarget: Ended - target: " + targetHeading + ", current: " + Robot.getAhrs().pidGet());
    	Robot.mecanumDrive.stop();
    	pidController.disable();
    	pidController = null;
    	sampleCount = 0;
    	samples = new SamplesSystem();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	System.out.println("CMD RotateToCenterVisionTarget: Interrupted - target: " + targetHeading + ", current: " + Robot.getAhrs().pidGet());
    	Robot.mecanumDrive.stop();
    	pidController.disable();
    	pidController = null;
    	sampleCount = 0;
    	samples = new SamplesSystem();
    }
    
    
    //remember that -Y is forwards
    public void pidWrite(double output) {
    	
    	if(this.cancel || this.isFinished()){ 
    		Robot.mecanumDrive.stop(); 
    	} else {
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
}
