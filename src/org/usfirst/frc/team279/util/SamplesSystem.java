package org.usfirst.frc.team279.util;

public class SamplesSystem {

	private final int    defaultSampleAmount = 10;
	private int          sampleAmount        = 0;
	private final double defaultValue        = -1.0;
	private double[]     samples;
	
	//** CONSTRUCTORS ******************************
	/***
	 * Sets up the sample system with the default sample amount
	 */
	public SamplesSystem() {
		sampleAmount = defaultSampleAmount;
		init();
	}

	
	/***
	 * Sets up the sample system with a given amount of samples
	 * @param amount amount samples to use
	 */
	public SamplesSystem(int amount) {
		sampleAmount = amount;
		init();
	}
	
	
	//** PRIVATE ***********************************
	private void init() {
		samples = new double[sampleAmount];
		setupSamples();
	}
	
	
	private void setupSamples() {
		for(int i=0; i<samples.length; i++) {
			samples[i] = defaultValue;
		}
	}
	
	
	//** PUBLIC ************************************
	/***
	 * Determines if all the default values have been written over
	 * @return false if samples still have the default value, else true
	 */
	public boolean checkSamples() {
		for(int i=0; i<samples.length; i++) {
			if(samples[i] == defaultValue) { return false; }
		}
		return true;
	}
	
	
	/***
	 * Sets the given sample to the last spot in the array and moves everything up
	 * @param value value of the sample
	 */
	public void setSample(double value) {
    	for(int i=0; i<samples.length; i++) {
    		if(i != samples.length-1) {
    			samples[i] = samples[i+1];
    		} else {
    			samples[i] = value;
    		}
    	}
    }
	
	
	//** GETTERS ***********************************
	/***
	 * Gives the samples array
	 * @return samples
	 */
	public double[] get() { 
		return samples;
	}
	
	
	/***
	 * Gives the last sample in the array of samples
	 * @return last sample
	 */
	public double getLastSample() { 
		return samples[samples.length-1];
	}
	
	
	/***
	 * Calculates the average of the samples and returns it
	 * @return average of samples
	 */
	public double getAverage() {
		double add = 0.0;
		for(int i=0; i<samples.length; i++) {
			add += samples[i];
		}
		return add/samples.length;
	}
}
