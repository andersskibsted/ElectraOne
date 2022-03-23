package com.electra;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Preferences;

public class ParameterMapping {
	
	public HashMap<String,int[]> deviceControlMappings = new HashMap<String,int[]>();
	private Preferences preferences;
	
	
	public ParameterMapping (Preferences preferences, ControllerHost host) {
		
		this.preferences = preferences;
		
		String parameterMappingPath = this.preferences.getStringSetting("Parameter Mappings", "Mapping file paths", 20, "/Users/andersskibsted/Documents/Bitwig Studio/Extensions/deviceParameterMappings.txt").get();
		
	      try {
	    	  File deviceData = new File(parameterMappingPath);
	          Scanner myReader = new Scanner(deviceData);
	          
	          while (myReader.hasNextLine()) {
	        	  String name = myReader.nextLine();
	        	  
	        	  int i = 0;
	        	  int[] mappings = new int[100];
	        	  while (myReader.hasNextInt()) {
	        		  mappings[i] = myReader.nextInt();
	        		  i++;
	        	  	}
	        	  this.deviceControlMappings.put(name, mappings);
	        	  
	            
	          }
	          myReader.close();
	        } catch (FileNotFoundException e) {
	          host.println("An error occurred.");
	          e.printStackTrace();
	        }
	 
	      
	}
}
