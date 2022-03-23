package com.electra;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.SettableEnumValue;

public class ProgramChangeHandler {
	
	CursorDevice cursorDevice;
	ControllerHost host;
	Preferences preferences;
	// String[] deviceNames;
	PluginParameters pluginParameters;
	DeviceIDs deviceIDs;
	NameSender nameSender;
	SettableEnumValue[] deviceCreate;
	
	
	public ProgramChangeHandler (CursorDevice cursorDevice, Preferences preferences, PluginParameters pluginParameters, DeviceIDs deviceIDs, NameSender nameSender,
			ControllerHost host) {
		
		this.cursorDevice = cursorDevice;
		this.host = host;
		this.preferences = preferences;
		this.pluginParameters = pluginParameters;
		this.deviceIDs = deviceIDs;
		//this.deviceNames = new String[deviceIDs.mappedDevices];
		this.deviceCreate = new SettableEnumValue[deviceIDs.mappedDevices];
		this.nameSender = nameSender;
		/*
		for (int i = 0; i < deviceIDs.mappedDevices; i++) {
			this.deviceNames[i] = deviceIDs.deviceNameAndID[i][0];
			
		}
		*/
		for (int i = 0; i < deviceIDs.mappedDevices; i++) {
			this.deviceCreate[i] = this.preferences.getEnumSetting("Insert " + Integer.toString(i), "Insert Device", deviceIDs.deviceNames, deviceIDs.deviceNames[i]);
			nameSender.sendOutSysexDeviceNames(this.deviceCreate[i].get(), i);
		}
		
		
		
	}
	
	public boolean handleMidi (ShortMidiMessage msg) {
		
		if (msg.isProgramChange()) {
			host.println(Integer.toString(msg.getData1()));
			
			switch (msg.getData1()) {
				
			case 0:
				cursorDevice.selectPrevious();
				return true;
			case 1: 
				cursorDevice.isEnabled().toggle();
				return true;
				
			case 2:				
				cursorDevice.isWindowOpen().toggle();
				return true;
				
			case 3: 
				cursorDevice.selectNext();
				return true;
			
				
			}
			
			for (int i = 4; i < deviceIDs.mappedDevices + 4; i++) {
				if (i == msg.getData1()) {
					cursorDevice.afterDeviceInsertionPoint().insertVST3Device(deviceIDs.deviceNameAndIDAsHashMap.get(deviceCreate[i - 4].get()));
					return true;
				}
			}
			
		}
		return false;
		
	}

}
