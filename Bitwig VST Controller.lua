function midi.onSysex (midiInput, sysexBlock)
	
	
	local t = {}
    local pluginName = {}
    local sysexID = sysexBlock:peek(3)

    -- device name
    if (sysexID == 9) then
	    
      for i = 1, sysexBlock:getLength() do
        table.insert(t, i, string.char(sysexBlock:peek(i)))    
      end

      local deviceName = table.concat(t, "", 4, #t - 1)
      groups.get(1):setLabel(deviceName)

    end

    -- parameter name
    if (sysexID == 2) then
      
      for i = 1, sysexBlock:getLength() do
        table.insert(t, i, string.char(sysexBlock:peek(i)))    
      end
      local parameterIndex = sysexBlock:peek(4)
      local parameterName = table.concat(t, "", 5, #t - 1)
      -- offset to leave permanent controls untouched
      if parameterIndex > 30 then parameterIndex = parameterIndex + 2 end

      controls.get(parameterIndex):setName(parameterName)

    end   

    -- device names for device insertion
    if (sysexID == 3) then
      for i = 1, sysexBlock:getLength() do
        table.insert(t, i, string.char(sysexBlock:peek(i)))
      end
      local controlIndex = sysexBlock:peek(4) + 397
      local deviceName = table.concat(t, "", 5, #t - 1)
      controls.get(controlIndex):setName(deviceName)
    end
end

function midi.onProgramChange (midiInput, channel, programNumber)
  
  if (programNumber == 127) then
    pages.display(1)
  end

end