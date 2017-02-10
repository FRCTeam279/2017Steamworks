package org.usfirst.frc.team279.util;

import edu.wpi.first.wpilibj.Preferences;


public class Config {
    private Preferences prefs = Preferences.getInstance();

    
    //--------------------------------------------------------------------------
    public Config() {
        //Utility.consoleMessage("Configuration Object Created");
    }
    
    //--------------------------------------------------------------------------
 
    // Load will check for the existence of a given key/value
    // if it doesn't exist, then it will add it with the default value specified, so that the preferences file is always complete
    //
    // This is also used to simply read the value as needed throughout runtime
    //
    // Overloaded for each datatype supported
    // boolean
    // double
    // float
    // int
    // long
    // string
    
    public boolean load(String keyName, boolean defaultValue) {
        boolean val;
        if(prefs.containsKey(keyName)) {
            val = prefs.getBoolean(keyName, defaultValue);
        } else {
            prefs.putBoolean(keyName, defaultValue);
            val = defaultValue;
        }
        //Utility.consoleMessage("Configuration value loaded: " + keyName + " - " + val);
        return val;
    }
    //--------------------------------------------------------------------------
    public double load(String keyName, double defaultValue) {
        double val;
        if(prefs.containsKey(keyName)) {
            val = prefs.getDouble(keyName, defaultValue);
        } else {
            prefs.putDouble(keyName, defaultValue);
            val = defaultValue;
        }
        //Utility.consoleMessage("Configuration value loaded: " + keyName + " - " + val);
        return val;
    }
    //--------------------------------------------------------------------------
    public float load(String keyName, float defaultValue) {
        float val;
        if(prefs.containsKey(keyName)) {
            val = prefs.getFloat(keyName, defaultValue);
        } else {
            prefs.putFloat(keyName, defaultValue);
            val = defaultValue;
        }
        //Utility.consoleMessage("Configuration value loaded: " + keyName + " - " + val);
        return val;
    }
    //--------------------------------------------------------------------------
    public int load(String keyName, int defaultValue) {
        int val;
        if(prefs.containsKey(keyName)) {
            val = prefs.getInt(keyName, defaultValue);
        } else {
            prefs.putInt(keyName, defaultValue);
            val = defaultValue;
        }
        //Utility.consoleMessage("Configuration value loaded: " + keyName + " - " + val);
        return val;
    }
    //--------------------------------------------------------------------------
    public long load(String keyName, long defaultValue) {
        long val;
        if(prefs.containsKey(keyName)) {
            val = prefs.getLong(keyName, defaultValue);
        } else {
            prefs.putLong(keyName, defaultValue);
            val = defaultValue;
        }
        //Utility.consoleMessage("Configuration value loaded: " + keyName + " - " + val);
        return val;
    }
    //--------------------------------------------------------------------------
    public String load(String keyName, String defaultValue) {
        String val;
        if(prefs.containsKey(keyName)) {
            val = prefs.getString(keyName, defaultValue);
        } else {
            prefs.putString(keyName, defaultValue);
            val = defaultValue;
        }
        //Utility.consoleMessage("Configuration value loaded: " + keyName + " - " + val);
        return val;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void put(String keyName, boolean value) {
        prefs.putBoolean(keyName, value);
        //Utility.consoleMessage("Configuration value set: " + keyName + " - " + value);
    }
    //--------------------------------------------------------------------------
    public void put(String keyName, double value) {
        prefs.putDouble(keyName, value);
        //Utility.consoleMessage("Configuration value set: " + keyName + " - " + value);
    }
    //--------------------------------------------------------------------------
    public void put(String keyName, float value) {
        prefs.putFloat(keyName, value);
        //Utility.consoleMessage("Configuration value set: " + keyName + " - " + value);
    }
    //--------------------------------------------------------------------------
    public void put(String keyName, int value) {
        prefs.putInt(keyName, value);
        //Utility.consoleMessage("Configuration value set: " + keyName + " - " + value);
    }
    //--------------------------------------------------------------------------
    public void put(String keyName, long value) {
        prefs.putLong(keyName, value);
        //Utility.consoleMessage("Configuration value set: " + keyName + " - " + value);
    }
    //--------------------------------------------------------------------------
    public void put(String keyName, String value) {
        prefs.putString(keyName, value);
        //Utility.consoleMessage("Configuration value set: " + keyName + " - " + value);
    }
    
    //--------------------------------------------------------------------------
    
    public void remove(String keyName) {
         if(prefs.containsKey(keyName)) {
             prefs.remove(keyName);
             System.out.println("Configuration value removed: " + keyName);
         } else {
            //Utility.consoleMessage("Configuration value doesn't exist to remove it: " + keyName);
         }
    }
    
    //--------------------------------------------------------------------------
    public void save(){
        prefs.save();
        System.out.println("Configuration Saved");
    }
    
    
    
}
