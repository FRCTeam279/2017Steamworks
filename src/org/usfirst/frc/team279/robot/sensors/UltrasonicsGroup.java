package org.usfirst.frc.team279.robot.sensors;

import java.util.ArrayList;
import java.util.Hashtable;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;



public class UltrasonicsGroup {
	//https://en.wikipedia.org/wiki/Speed_of_sound
//	public static final double SPEED_OF_SOUND_MPS = 343.21;	 						//meters per second
//	public static final double SPEED_OF_SOUND_MMPS = SPEED_OF_SOUND_MPS * 1000;	 	//meters per second
//	public static final double SPEED_OF_SOUND_FPS = 1126.017;						//feet per second	
//	public static final double SPEED_OF_SOUND_IPS = 1126.017 * 12;					//inches per second
//	
	
	
	public static final double SPEED_OF_SOUND_MPS = 343.21;	 						//meters per second
	public static final double SPEED_OF_SOUND_MMPS = SPEED_OF_SOUND_MPS * 1000;	 	//meters per second
	public static final double SPEED_OF_SOUND_FPS = 1126.017;						//feet per second	
	public static final double SPEED_OF_SOUND_IPS = SPEED_OF_SOUND_FPS * 12;		//inches per second
	
	
	private double pingTime = 10 * 1e-6;
	
	private static ultrasonicsThread t = null;
	
	//Singleton Design pattern, Lazy Loading
	private static UltrasonicsGroup instance;
	private UltrasonicsGroup(){}
	public static UltrasonicsGroup getInstance(){
		if(instance == null){
			instance = new UltrasonicsGroup();
		}
		return instance;
	}
	
	private Hashtable<String, Ultrasonic> ultrasonics = new Hashtable<String, Ultrasonic>();
	
	public synchronized void addUltrasonic(String label, int pingPort, int echoPort) {
		if(pingPort < 0 || echoPort < 0) {
			return;
		}
		Ultrasonic us = new Ultrasonic(pingPort, echoPort);
		ultrasonics.put(label,  us);
	}
	public synchronized void remUltrasonic(String label) {
		if(!ultrasonics.containsKey(label)){
			return;
		}
		ultrasonics.remove(label);
	}
	public synchronized void enableUltrasonic(String label) {
		if(!ultrasonics.containsKey(label)){
			return;
		}
		ultrasonics.get(label).enable();
	}
	public synchronized void disableUltrasonic(String label) {
		if(!ultrasonics.containsKey(label)){
			return;
		}
		ultrasonics.get(label).disable();
	}
	public void freeAll(){
		for(Ultrasonic u : ultrasonics.values()) {
			u.free();
		}
	}
	
	public void startReading(){
		if(t != null && t.isAlive() ) {
			return;
		}
		if(ultrasonics.isEmpty()){
			return;
		}
		
		t = new ultrasonicsThread();
		t.start();
	}
	public void stopReading(){
		t.enabled = false;
	}
	
	
	public synchronized double getDistanceFeet(String label){
		return ultrasonics.get(label).getDistanceFeet();
	}	
	public synchronized double getDistanceInches(String label){
			return ultrasonics.get(label).getDistanceInches();
	}
	public synchronized double getDistanceMeters(String label){
		return ultrasonics.get(label).getDistanceMeters();
	}
	public synchronized double getDistanceMillimeters(String label){
		return ultrasonics.get(label).getDistanceMillimeters();
	}
	
	
	public synchronized UltrasonicReading getReading(String label){
		if(ultrasonics.containsKey(label)) {
			Ultrasonic u = ultrasonics.get(label);
			return new UltrasonicReading(u.getTimeRead(), u.getPulseTime());
		} else {
			return null;
		}
	}
	
	public class UltrasonicReading{
		private long timeRead = -99;
		private double pulseTime = -99.0;
		
		public double getDistanceFeet(){
			if(pulseTime > 0.0) {
				return pulseTime * SPEED_OF_SOUND_FPS / 2.0;
			} else {
				return pulseTime;
			}
		}
		public double getDistanceInches(){
			if(pulseTime > 0.0) {
				return pulseTime * SPEED_OF_SOUND_IPS / 2.0;
			} else {
				return pulseTime;
			}
		}
		public double getDistanceMeters(){
			if(pulseTime > 0.0) {
				return pulseTime * SPEED_OF_SOUND_MPS / 2.0;
			} else {
				return pulseTime;
			}
		}
		public double getDistanceMillimeters(){
			if(pulseTime > 0.0) {
				return pulseTime * SPEED_OF_SOUND_MMPS / 2.0;
			} else {
				return pulseTime;
			}
		}
		
		public UltrasonicReading(long time, double pulse){
			timeRead = time;
			pulseTime = pulse;
		}
		
	}
	
	
	
	private class ultrasonicsThread extends Thread{
		public boolean enabled = true;
		
		public synchronized void run() {
			if(!ultrasonics.isEmpty()) {
				while (!Thread.currentThread().isInterrupted() && enabled) {
					try {
						for(Ultrasonic u : ultrasonics.values()) {
							if(u.enabled){
								u.ping(0.2); //just over 16'
							}
						}
						Timer.delay(0.02);
					} catch (Exception e) {
						enabled = false;
						return;
					}
				}
			}
		}
		
	}
	
	
	
	private class Ultrasonic {
		//activate checks fort this when the group is running
		private boolean free = false;  //was free() called...
		
		private boolean enabled = false; //is this enabled for ranging
		public void enable(){
			enabled = true;
		}
		public void disable(){
			enabled = false;
		}
		
		
		private int pingPort = 0;
		private int echoPort = 0;
		private Counter counter = null;
		
		private DigitalOutput pingChannel = null;
		private DigitalInput echoChannel = null;
		
		private double pingTimeSec = 10 * 1e-6;
		public double getPingTimeSec() {
			return pingTimeSec;
		}
		public void setPingTimeSec(double sec) {
			if(sec < 0.0) {
				return;
			}
			pingTimeSec = sec;
		}
		
		private long timeRead = 0;
		public long getTimeRead(){
			return timeRead;
		}
		
		private double pulseTime = 0;
		public double getPulseTime(){
			return pulseTime;
		}
		public double getDistanceFeet(){
			if(pulseTime > 0.0) {
				return pulseTime * SPEED_OF_SOUND_FPS / 2.0;
			} else {
				return -1.0;
			}
		}
		public double getDistanceInches(){
			if(pulseTime > 0.0) {
				return pulseTime * SPEED_OF_SOUND_IPS / 2.0;
			} else {
				return -1.0;
			}
		}
		public double getDistanceMeters(){
			if(pulseTime > 0.0) {
				return pulseTime * SPEED_OF_SOUND_MPS / 2.0;
			} else {
				return -1.0;
			}
		}
		public double getDistanceMillimeters(){
			if(pulseTime > 0.0) {
				return pulseTime * SPEED_OF_SOUND_MMPS / 2.0;
			} else {
				return -1.0;
			}
		}
		
		
		public Ultrasonic(int ping, int echo) {
			pingChannel = new DigitalOutput(ping);
			echoChannel = new DigitalInput(echo);
			 
			counter = new Counter(echoChannel);
			counter.setMaxPeriod(1.0);
			counter.setSemiPeriodMode(true);
			counter.reset();
		}
		
		public Ultrasonic(int ping, int echo, boolean createCounter) {
			 pingChannel = new DigitalOutput(ping);
			 echoChannel = new DigitalInput(echo);
			 
			 if(createCounter) {
				 counter = new Counter(echoChannel);
				 counter.setMaxPeriod(1.0);	
				 counter.setSemiPeriodMode(true);
				 counter.reset();
			 }
		}
		
		
		public void free(){
			free = true;
			enabled = false;
			if(counter != null) {
				counter.free();
			}
			
			pingChannel.free();
			echoChannel.free();
		}
		
		public void freeCounter(){
			if(counter != null) {
				counter.free();
				counter = null;
			}
		}
		
		public void setCounter(){
			if(counter != null) {
				counter.free();
				counter = null;
			}
			counter = new Counter(echoChannel);
			counter.setMaxPeriod(1.0);
			counter.setSemiPeriodMode(true);
			counter.reset();
		}
		
		//returns the duration of the pulse received
		// also saves this and the time the pulse was successfully read
		public double ping(double timeoutSec) throws InterruptedException {
			if(!enabled){
				return -1.0;
			}
			if(free){
				return -2.0;
			}
			if(counter == null){
				return -3.0;
			}
			pingChannel.pulse(pingTime);
			Timer.delay(.05); 
			long timeOutAt = System.currentTimeMillis() +(long)(timeoutSec * 1000);
			while(true) {
				if(counter.get() > 1) {
					timeRead = System.currentTimeMillis();
					pulseTime = counter.getPeriod();
					return pulseTime;
				} else {
					if(System.currentTimeMillis() > timeOutAt) {
						return Double.POSITIVE_INFINITY;
					}
					Timer.delay(0.02);
				}
			}
		}
	}
}
