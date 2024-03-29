package uk.org.toot.midi.sequence;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import javax.sound.midi.Track;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.InvalidMidiDataException;
import uk.org.toot.midi.message.PitchMsg;
import uk.org.toot.midi.sequence.edit.Transposable;
import uk.org.toot.midi.sequence.edit.CutPasteable;
import uk.org.toot.swingui.midiui.MidiColor;
import static uk.org.toot.midi.message.MetaMsg.*;
import static uk.org.toot.midi.message.NoteMsg.*;
import static uk.org.toot.midi.misc.Controller.BANK_SELECT;

public class MidiTrack extends BasicTrack implements Transposable, CutPasteable {

    /** @supplierCardinality 1 */
    protected MidiSequence sequence;

    private int program = -1;

    private int bank = -1;

    private int channel = -1;

    private Hashtable<Object, Object> properties;

    private PropertyChangeSupport propertyChangeSupport;

    public MidiTrack(Track track, MidiSequence sequence) {
        super(track);
        this.sequence = sequence;
        parse();
        updateHue(getTrackName());
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public MidiSequence getSequence() {
        return sequence;
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }

    public boolean add(MidiEvent event) {
        if (super.add(event)) {
            parseEvent(event);
            return true;
        }
        return false;
    }

    private long delayTicks = 0L;

    public long getDelayTicks() {
        return delayTicks;
    }

    public void setDelayTicks(long ticks) {
        delayTicks = ticks;
    }

    public MidiEvent get(int index) {
        MidiEvent event = super.get(index);
        if (delayTicks == 0) return event;
        return new MidiEvent(event.getMessage(), event.getTick() + delayTicks);
    }

    public boolean isDrumTrack() {
        return channel == (10 - 1) || channel == (11 - 1);
    }

    public boolean isMarkerTrack() {
        return getTrackName().equals("Marker");
    }

    public String propertyName(int type) {
        switch(type) {
            case TEXT:
                return "text";
            case COPYRIGHT:
                return "copyright";
            case TRACK_NAME:
                return "trackName";
            case INSTRUMENT_NAME:
                return "instrumentName";
            case LYRIC:
                return "lyric";
            case MARKER:
                return "marker";
            case CUE_POINT:
                return "cuepoint";
            case DEVICE_NAME:
                return "deviceName";
        }
        return null;
    }

    protected void setMetaName(int type, String name) throws InvalidMidiDataException {
        String oldName = getMetaName(type);
        super.setMetaName(type, name);
        getPropertyChangeSupport().firePropertyChange(propertyName(type), oldName, name);
    }

    public void setTrackName(String name) throws InvalidMidiDataException {
        updateHue(name);
        super.setTrackName(name);
    }

    protected void updateHue(String name) {
        float hue = isDrumTrack() ? MidiColor.getDrumHue(name) : MidiColor.getPitchedHue(name);
        putClientProperty("Hue", new Float(hue));
    }

    public String getProgramName() {
        return "prg. " + program;
    }

    public int getProgram() {
        return this.program;
    }

    protected MidiEvent getFirstProgramEvent() {
        for (int i = 0; i < size() - 1; i++) {
            MidiEvent event = get(i);
            MidiMessage msg = event.getMessage();
            if (isChannel(msg)) {
                if (getCommand(msg) == PROGRAM_CHANGE) {
                    return event;
                }
            }
        }
        return null;
    }

    public void setProgram(int prg) {
        if (prg == program) return;
        MidiMessage m;
        try {
            MidiEvent e = getFirstProgramEvent();
            if (e != null) {
                m = e.getMessage();
                setData1(m, prg);
                remove(e);
                add(e);
            } else {
                m = createChannel(PROGRAM_CHANGE, channel, prg, 0);
                add(new MidiEvent(m, 0));
            }
            int oldProgram = program;
            program = prg;
            getPropertyChangeSupport().firePropertyChange("Program", new Integer(oldProgram), new Integer(program));
        } catch (InvalidMidiDataException ex) {
            ex.printStackTrace();
        }
    }

    public int getBank() {
        return bank;
    }

    protected MidiEvent getFirstBankEvent() {
        for (int i = 0; i < size() - 1; i++) {
            MidiEvent event = get(i);
            MidiMessage msg = event.getMessage();
            if (isChannel(msg)) {
                if (getCommand(msg) == CONTROL_CHANGE && getData1(msg) == BANK_SELECT) {
                    return event;
                }
            }
        }
        return null;
    }

    public void setBank(int bank) {
        if (bank == this.bank) return;
        MidiMessage m;
        try {
            MidiEvent e = getFirstBankEvent();
            if (e != null) {
                m = e.getMessage();
                setData2(m, bank);
            } else {
                m = createChannel(CONTROL_CHANGE, channel, BANK_SELECT, bank);
                add(new MidiEvent(m, 0));
                MidiEvent pce = getFirstProgramEvent();
                if (pce != null) {
                    remove(pce);
                    add(pce);
                }
            }
            int oldBank = this.bank;
            this.bank = bank;
            getPropertyChangeSupport().firePropertyChange("Bank", new Integer(oldBank), new Integer(bank));
        } catch (InvalidMidiDataException ex) {
            ex.printStackTrace();
        }
    }

    public int getChannel() {
        return channel;
    }

    public void changeChannel(int channel) {
        for (int i = 0; i < size(); i++) {
            try {
                MidiEvent ev = get(i);
                MidiMessage msg = ev.getMessage();
                if (isChannel(msg)) {
                    int cmd = getCommand(msg);
                    switch(cmd) {
                        case CHANNEL_PRESSURE:
                        case CONTROL_CHANGE:
                        case NOTE_OFF:
                        case NOTE_ON:
                        case PITCH_BEND:
                        case POLY_PRESSURE:
                        case PROGRAM_CHANGE:
                            setChannel(msg, channel);
                            break;
                    }
                } else if (isMeta(msg)) {
                    byte[] bytes = getData(msg);
                    if (getType(msg) == CHANNEL_PREFIX) {
                        bytes[0] = (byte) channel;
                        setData(msg, bytes, bytes.length);
                    }
                }
            } catch (InvalidMidiDataException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean cut() {
        sequence.deleteTrack(this.getTrack());
        return true;
    }

    public boolean paste() {
        sequence.addTrack(this);
        return true;
    }

    public boolean transpose(int semitones) {
        for (int i = 0; i < size(); i++) {
            try {
                MidiMessage msg = get(i).getMessage();
                if (isPitch(msg)) {
                    PitchMsg.transpose(msg, semitones);
                }
            } catch (InvalidMidiDataException ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public List<MidiNote> getMatches(long startTick, int hiValue, long endTick, int loValue) {
        return getMatches((long) 0, startTick, hiValue, endTick, loValue);
    }

    public List<MidiNote> getMatches(long offsetTicks, long startTick, int hiValue, long endTick, int loValue) {
        ArrayList<MidiNote> matches = new ArrayList<MidiNote>();
        for (int i = 0; i < size(); i++) {
            MidiEvent on = get(i);
            if (on.getTick() < (startTick - offsetTicks)) continue;
            if (on.getTick() > endTick) break;
            MidiMessage msg = on.getMessage();
            if (isNote(msg)) {
                if (isOn(msg)) {
                    int note = getPitch(msg);
                    if ((loValue != -1 && note < loValue) || (hiValue != -1 && note > hiValue)) {
                        continue;
                    }
                    for (int j = 1 + i; j < size(); j++) {
                        MidiEvent off = get(j);
                        MidiMessage m = off.getMessage();
                        if (!isNote(m)) continue;
                        if (!isOff(m)) continue;
                        if (getPitch(m) != note) continue;
                        if (off.getTick() < startTick) break;
                        matches.add(new MidiNote(on, off));
                        break;
                    }
                }
            }
        }
        return matches;
    }

    protected void parse() {
        for (int i = 0; i < size(); i++) {
            parseEvent(get(i));
        }
    }

    protected void parseEvent(MidiEvent ev) {
        MidiMessage msg = ev.getMessage();
        if (isChannel(msg)) {
            int chan = uk.org.toot.midi.message.NoteMsg.getChannel(msg);
            if (this.channel < 0) {
                this.channel = chan;
            } else if (this.channel != chan) {
                System.err.println("Track " + getTrackName() + " has more than 1 channel!");
            }
            switch(getCommand(msg)) {
                case PROGRAM_CHANGE:
                    program = getData1(msg);
                    break;
                case CONTROL_CHANGE:
                    if (BANK_SELECT == getData1(msg)) {
                        bank = getData2(msg);
                    }
                    break;
            }
        }
    }

    public Hashtable<Object, Object> getProperties() {
        if (properties == null) {
            properties = new Hashtable<Object, Object>();
        }
        return properties;
    }

    public final Object getClientProperty(Object key) {
        return getProperties().get(key);
    }

    public void putClientProperty(Object key, Object value) {
        getProperties().put(key, value);
    }
}
