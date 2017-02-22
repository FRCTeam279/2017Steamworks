package org.usfirst.frc.team279.robot.commands;

import org.usfirst.frc.team279.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoMiddleGear extends CommandGroup {

    public AutoMiddleGear() {
    	addSequential(new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -458, 0.008, 0, 0, 20, 0.2, 1.0, -10000, 10000));
    	addSequential(new YawPID(-92, 0.008, 0, 0, 2, .2));
    	
    	addSequential(new Delay(300));
    	
    	//SquareToUltrasonics(String leftSensor, String rightSensor, double target, double p, double i, double d, double tolerance, double minSpeed)
    	addSequential(new SquareToUltrasonics("rangeGearLeft", "rangeGearRight", 12.0, 0.003, 0.0, 0.0, 0.5, 0.25), 3.0);
    	
    	System.out.println("Auto Middle Gear - starting USDD to 4inch");
    	//public DriveToUltrasonicDistance(String ultrasonicName, double target, double p, double i, double d, double tolerance, double minSpeed) {
    	addSequential(new DriveToUltrasonicDistanceX("rangeGearRight", 4.0, 0.006, 0.0, 0.0, 0.5, 0.3, 1.0, 0.0, 0.3), 4.0);
    	
    	System.out.println("Auto Middle Gear - Starting OpenGearDoor");
    	addSequential(new OpenGearDoor(), 1.0);
    	
    	System.out.println("Auto Middle Gear - starting USDD to 20inch");
    	addSequential(new DriveToUltrasonicDistanceY("rangeGearRight", 20, 0.006, 0.0, 0.0, 1.0, 0.3, 1.0, 0.0, 0.3), 4.0);
    	//stuff here for placing the gear
    }
}
