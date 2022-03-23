package com.electra;


import com.bitwig.extension.api.util.midi.ShortMidiMessage;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.CursorDeviceFollowMode;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.SettableStringValue;
import com.bitwig.extension.controller.ControllerExtension;

public class ElectraOneExtension extends ControllerExtension
{
	
	public static boolean isDeviceMapped;
	
	private ParameterHandler parameterHandler;
	private PluginParameters pluginParameters;
	private ElectraOneHardware electraOneHardware;
	private ProgramChangeHandler programChangeHandler;
	public String activeDevice;
	public SettableStringValue deviceIDPath;
	public SettableStringValue parameterMappingPath;
	public SettableStringValue programChangeMappingPath;
  

protected ElectraOneExtension(final ElectraOneExtensionDefinition definition, final ControllerHost host)
   {
      super(definition, host);
   }

   @Override
   public void init()
   {
      final ControllerHost host = getHost();
      final Preferences preferences = host.getPreferences();
      final CursorTrack cursorTrack = host.createCursorTrack ("ELECTRA_CURSOR_TRACK", "Cursor Track", 0, 0, true);
      final CursorDevice cursorDevice = cursorTrack.createCursorDevice("ELECTRA_DEVICE", "Cursor Device", 0, CursorDeviceFollowMode.FOLLOW_SELECTION);
      final NameSender nameSender = new NameSender(host.getMidiOutPort(0), this.getHost());
      final DeviceIDs deviceIDs = new DeviceIDs(preferences, this.getHost());
      final ParameterMapping parameterMapping = new ParameterMapping(preferences, this.getHost());
      
      
      this.electraOneHardware = new ElectraOneHardware(host.getMidiInPort(0), host.getMidiOutPort(0), this::handleMidi, host);
      this.pluginParameters = new PluginParameters(cursorTrack, this.electraOneHardware, cursorDevice, nameSender, deviceIDs, parameterMapping, host);
      this.parameterHandler = new ParameterHandler(this.pluginParameters, this.electraOneHardware, this.activeDevice, host);
      this.programChangeHandler = new ProgramChangeHandler(cursorDevice, preferences, pluginParameters, deviceIDs, nameSender, this.getHost());

      host.showPopupNotification("ElectraProto1 Initialized");
      
   }
   
   
   public void handleMidi (final int statusByte, final int data1, final int data2) {
	   
	   ShortMidiMessage msg = new ShortMidiMessage(statusByte, data1, data2);
	   
	   if (msg.isControlChange()) {
		   
		  this.electraOneHardware.updateHardwareValues(msg);
		   
		  if (this.parameterHandler.handleMidi(msg)) return;
		  
	   }
	   
	 // if (msg.isProgramChange()) {
		  
		 if  (this.programChangeHandler.handleMidi(msg)) return;
		  
	//  }
	   
	   else this.getHost().showPopupNotification("Midi unmapped and unprocessed");
   }

	  
   public void handleSysex (final String sysexMsg) {
	   
	   if (sysexMsg.isEmpty());
   }

   @Override
   public void exit()
   {
            
	   this.getHost().showPopupNotification("ElectraProto1 Exited");
      
   }

   @Override
   public void flush()
   {
      // TODO Send any updates you need here.
   }

}
