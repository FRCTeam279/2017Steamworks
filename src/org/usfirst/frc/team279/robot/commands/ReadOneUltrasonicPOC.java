package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a proof of concept command and should NOT be used on a competition robot
 * This code will block and prevent the robot from responding to input while running
 * 
 * ONLY use this code for understanding how pulse/echo ultrasonics work with the RoboRio
 */
public class ReadOneUltrasonicPOC extends Command {
	private static final double SPEED_OF_SOUND_MPS = 343.21;	 						//meters per second
	private static final double SPEED_OF_SOUND_MMPS = SPEED_OF_SOUND_MPS * 1000;	 	//meters per second
	private static final double SPEED_OF_SOUND_FPS = 1126.017;						//feet per second	
	private static final double SPEED_OF_SOUND_IPS = 1126.017 * 12;					//inches per second
	private double pingTime = 10 * 1e-6;

	private int pingPort = 11;
	private int echoPort = 10;
	
	private DigitalInput echoChannel;
	private DigitalOutput pingChannel;
	private Counter counter;
	
	
    public ReadOneUltrasonicPOC(int pingPort, int echoPort) {
    	super("ReadOneUltrasonicPOC");
                
        this.setInterruptible(true);
        this.setRunWhenDisabled(true);
        
        this.pingPort = pingPort;
        this.echoPort = echoPort;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	System.out.println("ReadOneUltrasonic: Creating channels and counter");
    	pingChannel = new DigitalOutput(pingPort);
		echoChannel = new DigitalInput(echoPort);
		 
		counter = new Counter(echoChannel);
		counter.setMaxPeriod(1.0);
		counter.setSemiPeriodMode(true);
		counter.reset();
		
		System.out.println("ReadOneUltrasonic: Waiting for reading...");
		pingChannel.pulse(pingTime);
		Timer.delay(.1); 
		
		long timeOutAt = System.currentTimeMillis() +(long)(0.2 * 1000);
		while(true) {
			if(counter.get() > 1) {
				double period = counter.getPeriod();
				System.out.println("ReadOneUltrasonic: Period Returned: " + Double.toString(period));
				
				//for whatever reason, the first reading is always 0.  Throwing it out avoids that
				if(Double.isInfinite(period)){
					counter.reset();
					pingChannel.pulse(pingTime);
					Timer.delay(.1); 
					continue;
				}
					
				double distance = period * SPEED_OF_SOUND_IPS / 2;
				System.out.println("ReadOneUltrasonic: Distance Measured " + distance);
				return;
			} else {
				if(System.currentTimeMillis() > timeOutAt) {
					System.out.println("ReadOneUltrasonic: Error - Timed out!!!");
					return;
				}
				Timer.delay(0.02);
			}
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	cleanUp();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	cleanUp();
    }
    
    protected void cleanUp(){
    	counter.free();
    	pingChannel.free();
    	echoChannel.free();
    	System.out.println("ReadOneUltrasonic: Cleaned Up");
    }
}
