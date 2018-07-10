import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.Clip;

import javax.sound.sampled.DataLine;

import javax.sound.sampled.LineEvent;

import javax.sound.sampled.LineListener;



/**

 EZ Sound Found

 Inspired by Sun's demo sound program Juke.java written by Brian Lichtenwalter.

 */

public class SoundPlayer implements LineListener, MetaEventListener {



    Sequencer mySequencer;



    boolean myMidiEomFlag, myAudioEomFlag;



    double myDuration;
