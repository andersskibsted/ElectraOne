package com.electra;

import java.nio.charset.StandardCharsets;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.api.util.midi.SysexBuilder;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.MidiOut;

public class NameSender {
	
	final MidiOut outPort;
	final SysexBuilder sysexBuilder = new SysexBuilder();
	final ControllerHost host;
	
	
	public NameSender (MidiOut outPort, ControllerHost host) {
		
		this.outPort = outPort;
		this.host = host;

		
	}
	

	
	public void sendoutSysex (byte[] input) {
			
			
		outPort.sendSysex(input);
		
		
	}

	public String stringToHex (String str) {
		
		StringBuilder stringBuilder = new StringBuilder();

        char[] charArray = str.toCharArray();

        for (char c : charArray) {
            String charToHex = Integer.toHexString(c);
            stringBuilder.append(charToHex);
            
        }
        
        return stringBuilder.toString();
		
		
	}
	
	public byte[] stringToASCII (String str) {
		
		byte[] bytes = str.getBytes(StandardCharsets.US_ASCII);
		
		return bytes;
	}

	public void sendoutNameAsSysex(String str) {
		
		byte[] sysexMsg = stringToASCII(str);
		byte[] sysexMsg2 = new byte[sysexMsg.length + 4];
		sysexMsg2[0] = (byte) 0xF0;
		sysexMsg2[1] = (byte) 0x00;
		sysexMsg2[2] = (byte) 0x09;
		// sysexMsg2[3] = (byte) 0x72;
		for (int i = 0; i < sysexMsg.length; i++) sysexMsg2[i+3] = sysexMsg[i];
		sysexMsg2[sysexMsg2.length - 1] = (byte) 0xF7;
		sendoutSysex(sysexMsg2);
		
		
	}
	
	
	public void sendOutSysexParameterName(String name, int j) {
		
		byte[] sysexMsg = stringToASCII(name);
		byte[] sysexMsg2 = new byte[sysexMsg.length + 5];
		sysexMsg2[0] = (byte) 0xF0;
		sysexMsg2[1] = (byte) 0x00;
		sysexMsg2[2] = (byte) 0x02;
		sysexMsg2[3] = (byte) j;
		for (int i = 0; i < sysexMsg.length; i++) sysexMsg2[i+4] = sysexMsg[i];
		sysexMsg2[sysexMsg2.length - 1] = (byte) 0xF7;
		sendoutSysex(sysexMsg2);
		
	}
	
	public void sendOutSysexDeviceNames(String name, int j) {
		
		byte[] sysexMsg = stringToASCII(name);
		byte[] sysexMsg2 = new byte[sysexMsg.length + 5];
		sysexMsg2[0] = (byte) 0xF0;
		sysexMsg2[1] = (byte) 0x00;
		sysexMsg2[2] = (byte) 0x03;
		sysexMsg2[3] = (byte) j;
		for (int i = 0; i < sysexMsg.length; i++) sysexMsg2[i+4] = sysexMsg[i];
		sysexMsg2[sysexMsg2.length - 1] = (byte) 0xF7;
		sendoutSysex(sysexMsg2);
		
	}

}
