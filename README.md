# ElectraOne

Controller extension for the Electra One midi controller for Bitwig.
This is so far only tested on OSX Monterey, but it should work on PC as well as previous OSX.

Put ElectraOne.bwextension in your Bitwig extension folder.
Put the two mapping and configuration .txt-files in the same folder.

Two do your own mappings, the way and format of the .txt-files are as follows:
(So far only VST3's are supported)

First you have to make a file called config.json and put it in your Bitwig Studio folder in Application Support (on Mac, I don't know where on PC, someone enlighten me and I will add it here)
In this config.json you out:

extension-dev: true
can-copy-device-and-param-ids : true



--------
I have added a config.json you can download here in the repository



deviceNameAndID.txt:
First the name of VST3 exactly as it is in Bitwig
Next the unique ID. You get that from right clicking on the device in Bitwig and pressing "Copy Device ID to Clipboard"
ex.

MDynamics
4D656C646170726F4D4165244D416524


deviceParameterMappings.txt:
First the name of VST3 exactly as it is in Bitwig
Next the parameter numbers seperated by one space. Some devices have meaningful numberings, some have not (Plugin Alliance plugins for example...)

MDynamics
0 64 65 71 72 75 76 66 67 90 91 95 96 79 80 92 93 97 98 81 82 85 86 87 88 70 69
