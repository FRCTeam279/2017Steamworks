package org.usfirst.frc.team279.robot.subsystems;

import org.usfirst.frc.team279.robot.Robot;
import org.usfirst.frc.team279.util.Config;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GearGizmo extends Subsystem {
	
	private String prefPrefix = "gg_";

	private int doorMotorPort = 7;

	private int openDoorSwitchPort = 20;
	private int closeDoorSwitchPort = 19;
	private int gearPosSwitchPort = 18;

	private Talon doorMotor;
	private double doorSpeed = 0.25;
	
	private DigitalInput openDoorSwitch;
	public DigitalInput getOpenDoorSwitch() {
		return openDoorSwitch;
	}
	
	private DigitalInput closeDoorSwitch;
	public DigitalInput getCloseDoorSwitch(){
		return closeDoorSwitch;
	}
	
	private boolean savedGearPos = false;
	private DigitalInput gearPosSwitch;
	public DigitalInput getGearPositionSwitch(){
		return gearPosSwitch;
	}
	
	private Counter openDoorCounter;
	private Counter closeDoorCounter;
	
	private boolean invertDoorMotor = false;

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
	
	public void init() throws RuntimeException {
		
		loadPrefs();
		System.out.println("GG: Preferences loaded");
		
		doorMotor = new Talon(doorMotorPort);
		doorMotor.setInverted(invertDoorMotor);
		System.out.println("GG: Speed Controllers Configured");
		
		openDoorSwitch = new DigitalInput(openDoorSwitchPort);
		openDoorCounter = new Counter(openDoorSwitch);
		
		closeDoorSwitch = new DigitalInput(closeDoorSwitchPort);
		closeDoorCounter = new Counter(closeDoorSwitch);
		System.out.println("GG: Limit Switches Setup");
		
		gearPosSwitch = new DigitalInput(gearPosSwitchPort);
		System.out.println("GG: Photo Eye Setup");
		
		this.resetCloseSwitch();
		this.resetOpenSwitch();
	}
	
	public void loadPrefs() {
		Config c = new Config();
		doorSpeed              = c.load(prefPrefix + "doorSpeed", doorSpeed);
		doorMotorPort          = c.load(prefPrefix + "doorMotorPort", doorMotorPort);
		openDoorSwitchPort     = c.load(prefPrefix + "openDoorSwitchPort", openDoorSwitchPort);
		closeDoorSwitchPort    = c.load(prefPrefix + "closeDoorSwitchPort", closeDoorSwitchPort);
		gearPosSwitchPort      = c.load(prefPrefix + "gearPosSwitchPort", gearPosSwitchPort);
		invertDoorMotor        = c.load(prefPrefix + "invertDoorMotor", invertDoorMotor);
	}
	
	
	//***DOOR MOTOR************************************************
	public void openDoor() {
		//if(!getOpenCount()){
			doorMotor.set(doorSpeed);
		//}
	}
	
	public void closeDoor () {
		//if(!getCloseCount()){
			doorMotor.set(-doorSpeed);
		//}
	}
	
	public void stopDoor() {
		doorMotor.stopMotor();
	}
	
	
	//***DOOR SWITCHES*********************************************
	public boolean getOpenCount() {
		return openDoorCounter.get() > 0;
	}
	
	public boolean getCloseCount() {
		return closeDoorCounter.get() > 0;
	}

	
	public void resetOpenSwitch() {
		openDoorCounter.reset();
	}
	
	public void resetCloseSwitch() {
		closeDoorCounter.reset();
	}
	
	public void saveGearPos() {
		savedGearPos = gearPosSwitch.get();
	}
	
//*** VISION GETTER'S ********************************************
    
    /**
     * Updates the Distance from the camera to the gear peg 
     * based on the information received from the Rasp PI through the
     * Network Tables
     * 
     * If {@value -1.0} is returned then an error occurred while
     * retrieving the value.
     * 
     * @return Distance from the camera to the target
     */
	public double getDistance() {
		try {
			return Robot.gearTable.getNumber("distance", 0.0);
		} catch(Exception e) {
    		System.err.println("NetworkTables Error: Failed to get a value for 'distance' from 'boilerTable'");
			return -1.0;
		}
    }
    
    
    /**
     * Updates the Rotation Angle of the robot based on the information
     * received from the Rasp PI through the Network Tables
     * 
     * If {@value -1.0} is returned then an error occurred while
     * retrieving the value.
     * 
     * @return Horz. Angle from the front of the camera to the target
     */
    public double getAngle() {
    	try {
    		return Robot.gearTable.getNumber("angle", 0.0);
    	} catch(Exception e) {
    		System.err.println("NetworkTables Error: Failed to get a value for 'angle' from 'boilerTable'");
    		return -1.0;
    	}
    }
    
    
    /**
     * Tells you if the camera sees the target
     * @return True if target is seen, False if target is not seen
     */
    public boolean getEyes() {
    	try {
    		return Robot.gearTable.getBoolean("eyes", false);
    	} catch(Exception e) {
    		System.err.println("NetworkTables Error: Failed to get a value for 'eyes' from 'boilerTable'");
    		return false;
    	}
    }
}

