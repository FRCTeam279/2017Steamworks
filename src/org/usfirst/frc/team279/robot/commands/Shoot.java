package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.util.SamplesSystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shoot extends Command {
	
	private SamplesSystem ultrasonicSamples;
	private SamplesSystem visionSamples;
	private int sampleAmount;

	private double distanceV;
	private double distanceU;
	private double distanceT;
	
	private double maxDistance;
	
	private boolean useVision = true;
	private boolean useUltra  = true;
	
    public Shoot(double sec) {
        requires(Robot.shooter);
        setInterruptible(true);
        setRunWhenDisabled(false);
        setTimeout(sec);
    }
    
    public Shoot() {
        requires(Robot.shooter);
        setInterruptible(true);
        setRunWhenDisabled(false);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	maxDistance = SmartDashboard.getNumber("Max Shooting Distance (inches)", 72);
    	sampleAmount = (int) SmartDashboard.getNumber("Set Sample Amount", 25);
    	ultrasonicSamples = new SamplesSystem(sampleAmount);
    	visionSamples = new SamplesSystem(sampleAmount);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Check Vision Distance
    	if(Robot.shooter.getEyes()) {
    		visionSamples.setSample(Robot.shooter.getDistance());
    		if(visionSamples.checkSamples()) {
    			distanceV = visionSamples.getAverage();
    			useVision = true;
	    	}
    	} else {
    		useVision = false;
    	}
    	
    	//Check Ultrasonic Distance
    	ultrasonicSamples.setSample(Robot.ultrasonics.getUltrasonics().getDistanceInches("rangeShooter"));
    	if(ultrasonicSamples.checkSamples()) {
    		distanceU = ultrasonicSamples.getAverage();
    		useUltra = true;
    	} else {
    		useUltra = false;
    	}
    	
    	if(useVision && useUltra) {
    		distanceT = (distanceV + distanceU)/2;
    		System.out.println("Vision and Ultra: " + distanceT);
    	} else if(useUltra && !useVision) {
    		distanceT = distanceU;
    		System.out.println("Ultra: " + distanceT);
    	} else if(!useUltra && useVision) {
    		distanceT = distanceV;
    		System.out.println("Vision: " + distanceT);
    	}
    	
    	if(distanceT <= maxDistance) {
    		SmartDashboard.putNumber("Shooter Range (Calculated Inches)", distanceT);
    		double spd = Robot.shooter.calcSpeedFromDistance(distanceT);
    		Robot.shooter.shootRPM(spd + 250);
    		System.out.println("Shoot CMD: Cur Spd:" + spd);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return this.isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooter.stopShooter();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.shooter.stopShooter();
    }
}
