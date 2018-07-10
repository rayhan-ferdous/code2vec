import javax.sound.midi.InvalidMidiDataException;

import javax.sound.midi.MidiMessage;

import javax.sound.midi.Receiver;

import javax.sound.midi.ShortMessage;

import java.util.Collection;



/** 

 * Allows to 'snoop' the data sent to a receiver, by passing data on to

 * MidiMessageListeners.

 * 

 * Note that instances of MidiMessageListener don't get directly connected to a

 * MonitorReceiver (there are no addMidiMessageListener() /

 * removeMidiMessageListener() methods on MonitorReceiver), but will be added to /

 * removed from higher-level classes that use MonitorReceivers.

 * 

 * @see MidiMessageListener

 * @author Jens Gulden

 */

public class MonitorReceiver implements Receiver {
