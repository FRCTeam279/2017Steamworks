package org.usfirst.frc.team279.util;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class NavHelper {
	
	public static final int xAxis = 0;
	public static final int yAxis = 1;
	public static final int zAxis = 2;
	
	
	public static double calcMagnitude(double x, double y){
		//c^2 = a^2 + b^2
		return java.lang.Math.sqrt((x * x) + (y * y));
	}
	
	//add degrees to a heading and ensure result is between 0 and 360;
	public static double addDegrees(double heading, double changeDegrees) {
		heading = heading + changeDegrees;
		return heading % 360;
	}
	
	
	//return a number -180 to +180
	public static double addDegreesYaw(double yaw, double changeDegrees) {
		yaw = yaw + changeDegrees;
		yaw = yaw % 360;
		if(yaw > 180.0) {
			return yaw - 360;
		}
		return yaw;
	}
	
	//converts a yaw value that the NavX supplies (-180 to +180) to a 0-360 value
	public static double convertYawToDegrees(double yaw){
		if(yaw == 0.0 || yaw == -0.0) { return 0.0; }
		if(yaw > 0.0) {
			return 360 % yaw;
		}
		if(yaw < 0.0) {
			yaw = yaw + 360;
			return 360 % yaw;
		}
		return 0.0;
	}
	
	
	public static double convertDegreesToYaw(double degrees){
		degrees = degrees % 360;
		if(degrees < 0.0) {
			degrees = degrees + 360;
		} 
		if (degrees <= 180.0) {
			return degrees;
		} else {
			return degrees - 360;
		}
	}
	
	public static final boolean TURNCW = true;
    public static final boolean TURNCCW = false;
    
    /********************************************
     * Determines what way to rotate the robot  *
     * @param newRot                            *
     * @return true=CW false=CCW                *
     ********************************************/
    public static boolean FindTurnDirection(double curRot, double newRot) {
    	//heading % 360;
    	if(newRot<180 && newRot>=0){
    		if(newRot>curRot)
    			return TURNCW;
    		else
    			return TURNCCW;
    	} else if(newRot<360 && newRot>=180){
    		if(newRot>curRot)
    			return TURNCW;
    		else
    			return TURNCCW;
    	} else
    		return false;
    }
	
    /********************************************
     * Determine the angle to the new location  *
     * @param newLoc                            *
     * @return angle							*
     ********************************************/
    public static double getAngleToNewLoc(Location currentLoc, Location newLoc) {
    	double distanceA = newLoc.getX() + currentLoc.getX();
    	System.out.println("DEBUG: distanceX = " + distanceA);
    	double distanceB = newLoc.getY() + currentLoc.getY();
    	System.out.println("DEBUG: distanceY = " + distanceB);
    	double angle = Math.atan2(distanceA, distanceB);
    	angle = Math.toDegrees(angle);
    	System.out.println("DEBUG: Angle before QUAD = " + angle);
    	final int QUAD_0 = 270;
    	final int QUAD_1 = 0;
    	final int QUAD_2 = 90;
    	final int QUAD_3 = 180;
    	
    	if(distanceB>=0 && distanceA == 0.0)
    		return 0.0;
    	else if(distanceB<0 && distanceA == 0.0)
    		return 180.0;
    	else if(distanceA>0 && distanceB == 0.0)
    		return 270.0;
    	else if(distanceA<0 && distanceB == 0.0)
    		return 90.0;
    		
    	//Find The Quad
    	if(distanceB > 0) {
    		if(distanceA > 0) {
    			System.out.println("QUAD_0");
    			return (90-angle) + QUAD_0;
    		} else {
    			System.out.println("QUAD_1");
    			return angle + QUAD_1;
    		}
    	} else {
    		if(distanceA < 0) {
    			System.out.println("QUAD_2");
    			return (90-angle) + QUAD_2;
    		} else {
    			System.out.println("QUAD_3");
    			return angle + QUAD_3;
    		}
    	}
    }
    
    /*********************************************
     * Determine the distance to the new location*
     * @param newLoc                             *
     * @return distance                          *
     *********************************************/
    public double getDistanceToNewLoc(Location currentLoc, Location newLoc) {
    	double distanceA = newLoc.getX() + currentLoc.getX();
    	double distanceB = newLoc.getY() + currentLoc.getY();
    	double distanceC = Math.hypot(distanceA, distanceB);
    	return distanceC;
    }


}
