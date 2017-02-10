package org.usfirst.frc.team279.util;

import edu.wpi.first.wpilibj.Joystick;

public class LF310Controller extends Joystick{

	private String ctrlName;
	public String getName(){
		return ctrlName;
	}
	
	
	 //--------------------------------------------------------------------------
    public LF310Controller(int port) {
        super(port, 6 , 12);
        ctrlName = "LF310";
    }
    
    //--------------------------------------------------------------------------
    public LF310Controller(String ctrlName, int port) {
        super(port, 6 , 12);
    }
    
    
  //Returns button or axis value
	public boolean getButton(int button) {
		return super.getRawButton(button);
	}
	
	public double getRawAxis(int axis){
		return getRawAxis(axis, false);
	}
	
	
	public double getRawAxis(int axis, boolean inverted) {
		if (inverted == true) {
			return super.getRawAxis(axis) * -1;
		} else {
	    return super.getRawAxis(axis);
		}
	}
}
