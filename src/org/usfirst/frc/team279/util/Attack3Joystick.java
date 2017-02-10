package org.usfirst.frc.team279.util;

import edu.wpi.first.wpilibj.Joystick;

public class Attack3Joystick extends Joystick{
	public Attack3Joystick(int port) {
		super(port);
		
		this.setAxisChannel(Joystick.AxisType.kX, 0);
		this.setAxisChannel(Joystick.AxisType.kY, 1);
		this.setAxisChannel(Joystick.AxisType.kZ, 2);
		this.setAxisChannel(Joystick.AxisType.kThrottle, 2);
		this.setAxisChannel(Joystick.AxisType.kTwist, 2);
	}
}
