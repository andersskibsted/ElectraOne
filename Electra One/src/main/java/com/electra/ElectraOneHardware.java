package com.electra;

import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiDataReceivedCallback;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.MidiIn;

public class ElectraOneHardware {
	
	public final static int NUMBER_OF_CONTROLS = 32;
	public final  MidiOut portOut;
	public final  MidiIn  portIn;
	public        int[]  hardwareValues = new int[33];
	private       ControllerHost host;
	
	
	
	
	public ElectraOneHardware(MidiIn inputPort, MidiOut outputPort, ShortMidiDataReceivedCallback inputCallback, ControllerHost host) { 
		
		this.portOut = outputPort;
		this.portIn = inputPort;
		this.portIn.setMidiCallback(inputCallback);
		this.host = host;
		
		this.host.println("hardware initialized");
		
		
		
	}

	public void updateHardwareValues (ShortMidiMessage msg) {
		
		if (msg.isControlChange()) {
			
			// update incoming 14-bit midi CC values and store it in hardware object 
			// so far only 32 parameters/controls
			if (msg.getData1() < 33) {
				
				// MSB
				int idx = msg.getData1();
				
				hardwareValues[idx] = (hardwareValues[idx] & (0x7F << 0)) | (msg.getData2() << 7);
				
			}
			else if (msg.getData1() >= 33 && msg.getData1() < 65) {
				 
				// LSB
				int idx = msg.getData1() - 32;
			
				hardwareValues[idx] = (hardwareValues[idx] & (0x7F << 7)) | (msg.getData2() << 0);
						
			}
			
		}
		
	}
	
	


	public void getDisplayedValuesFromElectra () {
		
		ShortMidiMessage requestValuesPrgrmMsg = new ShortMidiMessage(ShortMidiMessage.PROGRAM_CHANGE, 1, 127);
		
		this.portOut.sendMidi(requestValuesPrgrmMsg.getStatusByte(), requestValuesPrgrmMsg.getData1(), requestValuesPrgrmMsg.getData2());
	}
	
	public void updateDisplayValues (int parameterNumber) {
		// send out midi values for parameters associated with active control on electra
		
		
		this.portOut.sendMidi(parameterNumber, parameterNumber, parameterNumber);
	}
}
