package com.electra;


import java.util.Arrays;
import java.util.HashMap;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.Parameter;
import com.bitwig.extension.controller.api.RemoteControlsPage;
import com.bitwig.extension.controller.api.SpecificPluginDevice;

public class PluginParameters {
	
	final static int[] testDevice = new int[32];
	public String selectedDevice;
	
	
	// setup plugin names with unique ID's
	String[][] deviceNameAndID;
	DeviceIDs deviceIDs;
	ParameterMapping parameterMapping;
	
	final CursorTrack cursorTrack;
	final CursorDevice cursorDevice;
	final RemoteControlsPage remoteControlsBank;
	final NameSender NameSender;
	
	final HashMap<String,SpecificPluginDevice> vst3 = new HashMap<String,SpecificPluginDevice>();
	final HashMap<String,Parameter[]> deviceParameters = new HashMap<String,Parameter[]>();
	
	
	
	final ElectraOneHardware electraOneHardware;
	
	
	public PluginParameters (CursorTrack cursorTrack, ElectraOneHardware electraOneHardware, CursorDevice cursorDevice, NameSender nameSender,
				DeviceIDs deviceIDs, ParameterMapping parameterMapping, ControllerHost host) {
		
		this.cursorTrack = cursorTrack;
		this.cursorDevice = cursorDevice;
		this.NameSender = nameSender;
		this.deviceIDs = deviceIDs;
		this.parameterMapping = parameterMapping;
		this.remoteControlsBank = cursorDevice.createCursorRemoteControlsPage(33);
		this.electraOneHardware = electraOneHardware;
		
		
		// create plugin parameters from cursordevice
		
		for (int i = 0; i < deviceIDs.mappedDevices; i++) {
			
			String currentDeviceName = this.deviceIDs.deviceNameAndID[i][0];
			SpecificPluginDevice currentDevice = this.cursorDevice.createSpecificVst3Device(this.deviceIDs.deviceNameAndID[i][1]);
			
			vst3.put(currentDeviceName, currentDevice);
			
			
			
			// Parameter[] parameterArray = new Parameter[this.parameterMapping.deviceControlMappings.get(currentDeviceName).length];
			Parameter[] parameterArray = new Parameter[ElectraOneHardware.NUMBER_OF_CONTROLS + 1];
			

					
			//for (int j = 0; j < this.parameterMapping.deviceControlMappings.get(currentDeviceName).length; j++) {	
			for (int j = 1; j <= ElectraOneHardware.NUMBER_OF_CONTROLS; j++) {	
				
				int parameterNumber = this.parameterMapping.deviceControlMappings.get(currentDeviceName)[j];
				parameterArray[j] = currentDevice.createParameter(parameterNumber);
				
				parameterArray[j].markInterested();
				parameterArray[j].name().markInterested();
			
								
				int k = j;
				parameterArray[j].value().addValueObserver(16383, (value) -> 
				{  
					if (ElectraOneExtension.isDeviceMapped) {
						
						if (electraOneHardware.hardwareValues[k] != value) {
							
							int msb = ((value) >> 7) & 0x7F;
							int lsb = ((value) >> 0) & 0x7F;
							electraOneHardware.portOut.sendMidi(0xB0, k, msb);
							electraOneHardware.portOut.sendMidi(0xB0, k + 32, lsb);
							
							
						};
						
						electraOneHardware.hardwareValues[k] = value;
						
						};
				});
				
				
				this.remoteControlsBank.getParameter(j-1).markInterested();
				this.remoteControlsBank.getParameter(j-1).value().addValueObserver(16383, (value) ->
				{
					if (!ElectraOneExtension.isDeviceMapped) {
						
						if (electraOneHardware.hardwareValues[k] != value) {
							
							int msb = ((value) >> 7) & 0x7F;
							int lsb = ((value) >> 0) & 0x7F;
							electraOneHardware.portOut.sendMidi(0xB0, k, msb);
							electraOneHardware.portOut.sendMidi(0xB0, k + 32, lsb);
							
							
						};
						
						electraOneHardware.hardwareValues[k] = value;
						
					}
					
				});
				
				
				
				parameterArray[j].name().addValueObserver(name -> {
					
					if (ElectraOneExtension.isDeviceMapped) {
					
						nameSender.sendOutSysexParameterName(name, k);
						// host.getMidiOutPort(0).sendMidi(0xC0, 127, 127);
						
					}
					
				});
				
				remoteControlsBank.getParameter(j-1).name().markInterested();
				
				remoteControlsBank.getParameter(j-1).name().addValueObserver(name -> {
					
					if (!ElectraOneExtension.isDeviceMapped) {
					
						nameSender.sendOutSysexParameterName(name, k);
						
						
					}
					
				});
		
		
			}
			
			deviceParameters.put(currentDeviceName, parameterArray);
		
		}
		
		
		// on device selection, register callback functions for name change. Also for parameters
		cursorDevice.name().markInterested();
		cursorDevice.position().addValueObserver(value -> {
			
			selectedDevice = cursorDevice.name().get();
			
			ElectraOneExtension.isDeviceMapped = Arrays.asList(deviceIDs.deviceNames).contains(selectedDevice);
			
			nameSender.sendoutNameAsSysex(selectedDevice);
			
			host.getMidiOutPort(0).sendMidi(0xC0, 127, 127);
			
			
			
			
			
		});
		
		
	}
	
	public Parameter[] getParameterArray (String deviceName) {
		
		return this.deviceParameters.get(deviceName);
	}
	
	public Parameter getSpecificParameter (String deviceName, int index) {
		
		return this.deviceParameters.get(deviceName)[index];
		
	}
	
	

}
