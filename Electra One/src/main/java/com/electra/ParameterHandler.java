package com.electra;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.ControllerHost;

public class ParameterHandler {
	
	public PluginParameters   pluginParameters;
	final  ElectraOneHardware electraOneHardware;
		   String             activeDevice;
	final  ControllerHost     host;	   
	
	public ParameterHandler (PluginParameters pluginParameters, ElectraOneHardware electraOneHardware, String activeDevice, ControllerHost host) {
		
		this.pluginParameters = pluginParameters;
		this.electraOneHardware = electraOneHardware;
		this.activeDevice = activeDevice;
		this.host = host;
	}
	
	public boolean handleMidi (ShortMidiMessage msg) {
		
		 
				
			if (msg.isControlChange()) {
				
				if (msg.getData1() <= 32) {
					
					int idx = msg.getData1();
					
					electraOneHardware.hardwareValues[idx] = (electraOneHardware.hardwareValues[idx] & (0x7F << 0)) | (msg.getData2() << 7);
					
					if (ElectraOneExtension.isDeviceMapped) this.pluginParameters.getParameterArray(pluginParameters.selectedDevice)[idx].value().set(electraOneHardware.hardwareValues[idx], 16383);
					
					else this.pluginParameters.remoteControlsBank.getParameter(idx-1).value().set(electraOneHardware.hardwareValues[idx], 16383);
					
					return true;
				
				}
				
				else if (msg.getData1() >= 33 && msg.getData1() <= 64) {
					
					int idx = msg.getData1() - 32;
					
					electraOneHardware.hardwareValues[idx] = (electraOneHardware.hardwareValues[idx] & (0x7F << 7)) | (msg.getData2() << 0);
	
					if (ElectraOneExtension.isDeviceMapped) this.pluginParameters.deviceParameters.get(pluginParameters.selectedDevice)[idx].value().set(electraOneHardware.hardwareValues[idx], 16383);
					
					else this.pluginParameters.remoteControlsBank.getParameter(idx-1).value().set(electraOneHardware.hardwareValues[idx], 16383);
					
					return true;
				
				}
				
				else return false;
				
			}
		
		
		
		
		return false;
		
		
	}

}
