
package org.usfirst.frc.team279.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.SPI;

import org.usfirst.frc.team279.robot.commands.*;
import org.usfirst.frc.team279.robot.subsystems.*;
import org.usfirst.frc.team279.util.Config;

import com.kauailabs.navx.frc.AHRS;




public class Robot extends IterativeRobot {
	
	private String prefPrefix = "robot_";
	
	private static AHRS ahrs = null;
	public static AHRS getAhrs(){
		if(ahrs == null) {
			try {
		          /* Communicate w/navX-MXP via the MXP SPI Bus.                                     */
		          /* Alternatively:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB     */
		          /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
		          Robot.ahrs = new AHRS(SPI.Port.kMXP); 
		      } catch (RuntimeException ex ) {
		          DriverStation.reportError("Robot: Error instantiating navX-MXP:  " + ex.getMessage(), true);
		      }
		}
		return ahrs;
	}
	
	
	private double ahrsGyroAdjustment = 0.0;
	public double getAhrsGyroAdjustment(){
		return ahrsGyroAdjustment;
	}
	
	//--------------------------------------------------------------------------
	
	public static final MecanumDrive mecanumDrive = new MecanumDrive();
	public static final Ultrasonics ultrasonics = new Ultrasonics();
	public static OI oi;
	
	public static NetworkTable boilerTable;
	public static NetworkTable gearTable; 
	
	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	
	
	public void loadPrefs(){
		Config c = new Config();
		ahrsGyroAdjustment = c.load(prefPrefix + "ahrsGyroAdjustment", ahrsGyroAdjustment);
	}
	
	

	public void robotInit() {
		
		//Setup Tables for Vision
		NetworkTable.initialize();
		boilerTable = NetworkTable.getTable("Boiler");
		gearTable   = NetworkTable.getTable("Gear");
				
		Robot.mecanumDrive.init();
		Robot.ultrasonics.init();
		
		oi = new OI();
		oi.init();
	
		Robot.getAhrs().setAngleAdjustment(ahrsGyroAdjustment);
		
		chooser.addDefault("Default Auto", new DefaultAuto());
		//chooser.addObject("Rotate Angle Degrees", new RotateAngleDegrees(45.0, 0.3));
		//chooser.addObject("AutoDriveForward", new AutoDriveFoward());
		SmartDashboard.putData("Auto mode", chooser);
		
		
		
		//SmartDashboard.putData("Save Config",new SaveConfig());
	}

	
	//--------------------------------------------------------------------------
	@Override
	public void robotPeriodic() {
	}
	

	public void disabledInit() {

	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	
	
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}
	
	
	
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	
	
	
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	
}
