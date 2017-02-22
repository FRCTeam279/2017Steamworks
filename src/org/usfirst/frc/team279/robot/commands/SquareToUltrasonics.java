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
public class SquareToUltrasonics extends Command implements PIDSource, PIDOutput {
	private boolean useSmartDashBoardValues = false;

	public PIDController pidController;
	private String left;
	private String right;
    private double target = 0.0;
    private double kP = 0.000;
    private double kI = 0.000;
    private double kD = 0.000;
    private double kF = 0.000;
    private double kTolerance = 0.0;
    private double minSpeed = -1.0;
    private double maxSpeed = 1.0;
    private double minInput = 0;
    private double maxInput = 360;
    
    private boolean cancel = false;
    
    public SquareToUltrasonics(String leftSensor, String rightSensor) {
    	super("SquareToUltrasonics");
        requires(Robot.mecanumDrive);
        left = leftSensor;
        right = rightSensor;
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);

        useSmartDashBoardValues = true;
    }
    
    
    public SquareToUltrasonics(String leftSensor, String rightSensor, double target, double p, double i, double d, double tolerance) {
    	super("SquareToUltrasonics");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;
        left = leftSensor;
        right = rightSensor;
        this.target = target;
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kTolerance = Math.abs(tolerance);        
    }
    
    public SquareToUltrasonics(String leftSensor, String rightSensor, double target, double p, double i, double d, double tolerance, double minSpeed) {
    	super("SquareToUltrasonics");
        requires(Robot.mecanumDrive);
        
        this.setInterruptible(true);
        this.setRunWhenDisabled(false);
        useSmartDashBoardValues = false;
        left = leftSensor;
        right = rightSensor;
        this.target = target;
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
    	
    	if(useSmartDashBoardValues) {
	    	target = SmartDashboard.getNumber("TurnPID Target", 0.0);
			kP = SmartDashboard.getNumber("TurnPID P", 0.002);
			kI = SmartDashboard.getNumber("TurnPID I", 0.00);
			kD = SmartDashboard.getNumber("TurnPID D", 0.0);
			minSpeed = SmartDashboard.getNumber("TurnPID MinSpeed", 0.15);
			kTolerance = Math.abs(SmartDashboard.getNumber("TurnPID Tolerance", 2.0));
    	} 

    	pidController = new PIDController(kP, kI, kD, kF, this, this);
    	pidController.setInputRange(-30, 30);
    	pidController.setOutputRange(-1.0, 1.0);
    	pidController.setContinuous(false);
    	pidController.setAbsoluteTolerance(kTolerance);
        pidController.setSetpoint(target);
        System.out.println("CMD SquareToUltrasonics: Starting - target: " + target + ", current: " + this.pidGet());
    }

   
    protected void execute() {
    	if(pidController == null) {
    		this.initialize();
    	}
    	if(!pidController.isEnabled()){
    		pidController.enable();
    	}
    }

    
    protected boolean isFinished() {
    	if(this.cancel) {
    		return true;
    	}
    	return pidController.onTarget();
    }

    
    protected void end() {
    	System.out.println("CMD SquareToUltrasonics: Ended - target: " + target + ", current: " + this.pidGet());
    	Robot.mecanumDrive.stop();
    	pidController.disable();
    	pidController = null;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	System.out.println("CMD SquareToUltrasonics: Interrupted - target: " + target + ", current: " + this.pidGet());
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
    	double leftVal = 0.0;
    	double rightVal = 0.0;
    	
    	int countInvalid = 0;
    	//boolean keepCounting = true;
    	//while(keepCounting) {
    		leftVal = Robot.ultrasonics.getUltrasonics().getDistanceInches(left);
    		if(leftVal < 0.0 || leftVal > 250){
        		//countInvalid++;
        		//if(countInvalid == 3) {
        		System.out.println("CMD SquareToUltrasonics: Warning! Invalid measurements received on left side.. ending command");
        		this.cancel = true;
        		return 0.0;
           	//} 
        	//} else {
        	//	keepCounting = false;
        	}
    	//}
    	
    	countInvalid = 0;
    	//keepCounting = true;
    	//while(keepCounting) {
    		rightVal = Robot.ultrasonics.getUltrasonics().getDistanceInches(right);
    		if(rightVal < 0.0 || rightVal > 250){
        		//countInvalid++;
        		//if(countInvalid == 3) {
            		System.out.println("CMD SquareToUltrasonics: Warning! Invalid measurements received on right side.. ending command");
            		this.cancel = true;
            		return 0.0;
            	//} 
        		//else {
            	//	keepCounting = false;
            	//}
        	} 
    	//}
    	
    	return leftVal - rightVal;
    }
    
    
    //remember that -Y is forwards
    public void pidWrite(double output) {
    	
    	if(this.cancel){ 
    		Robot.mecanumDrive.stop(); 
    	} else {
    		if(Math.abs(output) < this.minSpeed) {
    			double tSpd = minSpeed;
    			if(output > 0.0) {
    				tSpd = minSpeed * -1.0;
    			} 
				//System.out.println("CMD SquareToUltrasonics: Output=" +  output + " minspeed=" + tSpd);
				Robot.mecanumDrive.getRoboDrive().mecanumDrive_Cartesian(0.0, 0.0, tSpd, 0.0);
    			
    		} else {
    			//System.out.println("CMD SquareToUltrasonics: Output=" +  output);
    			Robot.mecanumDrive.getRoboDrive().mecanumDrive_Cartesian(0.0, 0.0, output, 0.0);
    		}
    	}
    }
}
