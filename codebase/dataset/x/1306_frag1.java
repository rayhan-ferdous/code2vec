    public boolean init(MidiDevice inDev, MidiDevice outDev) {

        try {

            setKeyboardInDevice(inDev);

            setMidiOutDevice(outDev);

        } catch (Exception ex) {

            ex.printStackTrace();

            return false;

        }

        return true;

    }
