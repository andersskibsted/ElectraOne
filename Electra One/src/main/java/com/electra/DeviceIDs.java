package com.electra;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Preferences;

public class DeviceIDs {
	
	public HashMap<String,int[]> deviceControlMappings = new HashMap<String,int[]>();
	public int mappedDevices;
	public String[][] deviceNameAndID = new String[100][2];
	public HashMap<String,String> deviceNameAndIDAsHashMap = new HashMap<String,String>();
	private Preferences preferences;
	public String[] deviceNames;
	
	
	public DeviceIDs (Preferences preferences, ControllerHost host) {
		
		this.preferences = preferences;
		String deviceIDPath = this.preferences.getStringSetting("Device IDs", "Mapping file paths", 20, "/Users/andersskibsted/Documents/Bitwig Studio/Extensions/deviceNameAndID.txt").get();
		
	      try {
	          // File deviceData = new File("/Users/andersskibsted/Documents/Bitwig Studio/Extensions/deviceNameAndID.txt");
	    	  // File root = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
	    	  
	    	  File deviceData = new File(deviceIDPath);
	          Scanner myReader = new Scanner(deviceData);
	          int i = 0;
	          while (myReader.hasNextLine()) {
	        	  
	        	  String name = myReader.nextLine();
	        	  String id   = myReader.nextLine();
	        	  this.deviceNameAndID[i][0] = name;
	        	  this.deviceNameAndID[i][1] = id;
	        	  deviceNameAndIDAsHashMap.put(name, id);
	        	  i++;
	            
	          }
	          this.mappedDevices = i;
	          
	          this.deviceNames = new String[mappedDevices];
	          
	          for (int j = 0; j < this.mappedDevices; j++) {
	        	  
	  			this.deviceNames[j] = this.deviceNameAndID[j][0];
	  			
	  			}
	          
	          myReader.close();
	          
	        } catch (FileNotFoundException e) {
	          host.println("An error occurred.");
	          e.printStackTrace();
	        }
		
		
	}
}
