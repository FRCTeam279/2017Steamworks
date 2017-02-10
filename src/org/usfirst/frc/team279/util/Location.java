package org.usfirst.frc.team279.util;

public class Location {
	private double LocationX = 0.0;
	private double LocationY = 0.0;
	private double LocationZ = 0.0;
	private double Rotation  = 0.0;
	
	public Location(double x, double y, double z, double r) {
		LocationX = x;
		LocationY = y;
		LocationZ = z;
		Rotation = r;
	}
	
	public double getX() {
		return LocationX;
	}
	public void setX(double locationX) {
		LocationX = locationX;
	}
	public double getY() {
		return LocationY;
	}
	public void setY(double locationY) {
		LocationY = locationY;
	}
	public double getZ() {
		return LocationZ;
	}
	public void setZ(double locationZ) {
		LocationZ = locationZ;
	}
	public double getRotation() {
		return Rotation;
	}
	public void setRotation(double rotation) {
		Rotation = rotation;
	}
	
}