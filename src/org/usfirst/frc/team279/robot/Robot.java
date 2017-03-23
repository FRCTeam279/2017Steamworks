
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
	
	private static boolean setForTesting = true; //allow commands to query if we ra trying to set for competition or testing
	public static boolean getSetForTesting(){
		return setForTesting;
	}
	
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
	
	public static final MecanumDrive 	mecanumDrive = new MecanumDrive();
	public static final Ultrasonics  	ultrasonics  = new Ultrasonics();
	public static final Harvelator   	harvelator   = new Harvelator();
	public static final Shooter      	shooter      = new Shooter();
	public static final Feeder       	feeder       = new Feeder();
	public static final GearGizmo    	geargizmo    = new GearGizmo();
	public static final CamLightsShooter camLightShooter = new CamLightsShooter();
	public static final CamLightsGear 	camLightsGear = new CamLightsGear();
	public static final Climber climber = new Climber();
	public static final LedStrip ledStrip = new LedStrip();
	public static final GearPusher gearPusher = new GearPusher();
	
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
				
		//Subsystem Init's -- Start
		try {
			Robot.mecanumDrive.init();
		} catch(RuntimeException e) {
			DriverStation.reportError("Robot: Error instantiating MecanumDrive:  " + e.getMessage(), true);
		}
		
		try {
			Robot.climber.init();
		} catch(RuntimeException e) {
			DriverStation.reportError("Robot: Error instantiating Climber:  " + e.getMessage(), true);
		}
		
		try {
			Robot.shooter.init();
		} catch(RuntimeException e) {
			DriverStation.reportError("Robot: Error instantiating Shooter:  " + e.getMessage(), true);
		}
		
		try {
			Robot.feeder.init();
		} catch(RuntimeException e) {
			DriverStation.reportError("Robot: Error instantiating Feeder:  " + e.getMessage(), true);
		}
		
		try {
			//the subsystem is set to have the two ultrasonics disabled to start.. we can start the thread and then just enable them 
			// sensors when desired
			Robot.ultrasonics.init();
			Robot.ultrasonics.startUltrasonics();
		} catch(RuntimeException e) {
			DriverStation.reportError("Robot: Error instantiating Ultrasonics:  " + e.getMessage(), true);
		}
		
		try {
			Robot.geargizmo.init();
		} catch(RuntimeException e) {
			DriverStation.reportError("Robot: Error instantiating GearGizmo:  " + e.getMessage(), true);
		}
		
		try {
			Robot.harvelator.init();
		} catch(RuntimeException e) {
			DriverStation.reportError("Robot: Error instantiating Harvelator:  " + e.getMessage(), true);
		}
		
		try {
			Robot.gearPusher.init();
		} catch(RuntimeException e) {
			DriverStation.reportError("Robot: Error instantiating GearPusher:  " + e.getMessage(), true);
		}
		
		try {
			camLightsGear.init();
		} catch(RuntimeException e) {
			DriverStation.reportError("Robot: Error instantiating Gear Lights:  " + e.getMessage(), true);
		}
		
		try {
			ledStrip.init();
		} catch(RuntimeException e) {
			DriverStation.reportError("Robot: Error instantiating LED Strip:  " + e.getMessage(), true);
		}
		//Subsystem Init's -- End
		
		
		//Setup Tables for Vision
		try {
			NetworkTable.initialize();
			boilerTable = NetworkTable.getTable("Boiler");
			gearTable   = NetworkTable.getTable("Gear");
		} catch(Exception e) {
			DriverStation.reportError("Robot: Error instantiating NetworkTables:  " + e.getMessage(), true);
		}
		
		oi = new OI();
		oi.init();

		//Robot.getAhrs().setAngleAdjustment(ahrsGyroAdjustment);
		
		
		//SmartDashboard.putNumber("TurnPID Target", 0.0);
		//SmartDashboard.putNumber("TurnPID P", 0.0001);
		//SmartDashboard.putNumber("TurnPID I", 0.00);
		//SmartDashboard.putNumber("TurnPID D", 0.0);
		//SmartDashboard.putNumber("TurnPID MinSpeed", 0.15);
		//SmartDashboard.putNumber("TurnPID Tolerance", 5.0);
		//SmartDashboard.putData("TurnPID Execute",new YawPID());
		//SmartDashboard.putData("SquareToUS", new SquareToUltrasonics("rangeGearLeft", "rangeGearRight"));
		//SmartDashboard.putData("RotateTocenterVisionTarget Gear", new RotateToCenterVisionTarget("Gear", "pixelOffset"));
		
		//SmartDashboard.putNumber("DriveEnc Dir",  0.0);
		//SmartDashboard.putNumber("DriveEnc Target", 0.0);
		//SmartDashboard.putNumber("DriveEnc P", 0.00035);
		//SmartDashboard.putNumber("DriveEnc Tolerance", 50);
		//SmartDashboard.putNumber("DriveEnc minSpeed", 0.15);
		//SmartDashboard.putData("DriveEnc Execute LFEnc", new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront()));
		//SmartDashboard.putData("DriveEnc Execute RFEnc", new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderRightFront()));
		
		SmartDashboard.putData("Gear Push",(new GearPusherPush()));
		SmartDashboard.putData("Gear Retract",(new GearPusherRetract()));
		
		//** AUTO CHOOSER **************************************
		chooser.addDefault("Default Auto", new DefaultAuto());
		chooser.addObject("Middle Gear", new AutoMiddleGear());
		chooser.addObject("Left Gear", new AutoLeftGear());
		chooser.addObject("Right Gear", new AutoRightGear());
		chooser.addObject("Drive Past Line", new DriveToEncoderDistance(Robot.mecanumDrive.getEncoderLeftFront(), 0, -1146.5, 0.00035, 0, 0, 20, 0.25, 1.0, -10000, 10000));
		chooser.addObject("Shoot - Boiler Left", new AutoShootingBoilerOnLeft());
		chooser.addObject("Shoot - Boiler Right", new AutoShootingBoilerOnRight());
		SmartDashboard.putData("Auto Chooser", chooser);		
		
		
		SmartDashboard.putData("Save Config",new SaveConfig());
		SmartDashboard.putData("Reset Gyro",new ResetGyro());
		SmartDashboard.putData("Reset Encoders",new ResetEncoders());
		
		SmartDashboard.putData("Toggle Shooter Lights",new ShooterCamLightsToggle());
		SmartDashboard.putData("GearLightLow Toggle", new GearCamLightToggleLow());
		SmartDashboard.putData("GearLightHigh Toggle", new GearCamLightToggleHigh());
	}

	
	//--------------------------------------------------------------------------
	@Override
	public void robotPeriodic() {
		SmartDashboard.putNumber("LF Encoder Val", mecanumDrive.getEncoderLeftFront().get());
		//SmartDashboard.putNumber("LR Encoder Val", mecanumDrive.getEncoderLeftRear().get());
		SmartDashboard.putNumber("RF Encoder Val", mecanumDrive.getEncoderRightFront().get());
		//SmartDashboard.putNumber("RR Encoder Val", mecanumDrive.getEncoderRightRear().get());
		//SmartDashboard.putBoolean("LS Open", geargizmo.getOpenDoorSwitch().get());
		//SmartDashboard.putBoolean("LS Close", geargizmo.getCloseDoorSwitch().get());
		//SmartDashboard.putBoolean("LS Open2", geargizmo.getOpenCount());
		//SmartDashboard.putBoolean("LS Close2", geargizmo.getCloseCount());
		
		
		//Permanent
		SmartDashboard.putBoolean("GearVertical", Robot.geargizmo.getGearPositionSwitch().get());
		SmartDashboard.putNumber("Shooter Range (Ultra Inches)", Robot.ultrasonics.getUltrasonics().getDistanceInches("rangeShooter"));

		SmartDashboard.putNumber("Gear Range Left", ultrasonics.getUltrasonics().getReading("rangeGearLeft").getDistanceInches());
		SmartDashboard.putNumber("Gear Range Right", ultrasonics.getUltrasonics().getReading("rangeGearRight").getDistanceInches());
		
	}
	

	public void disabledInit() {

	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	
	
	public void autonomousInit() {
		//
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
		oi.checkForAxisButtons();
	}

	
	
	
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	
}
