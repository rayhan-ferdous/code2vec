        endBeatTextField.setText(Double.toString(Math.round(MIDIBeast.bassPart.getPhrase(0).getEndTime())));

    }



    public void setDrumDefaults() {

        startBeatTextField.setText(Double.toString(Math.round(MIDIBeast.drumPart.getPhrase(0).getStartTime())));

        endBeatTextField.setText(Double.toString(Math.round(MIDIBeast.drumPart.getPhrase(0).getEndTime())));

    }



    public void setChordDefaults() {

        startBeatTextField.setText(Double.toString(Math.round(MIDIBeast.chordPart.getPhrase(0).getStartTime())));

        endBeatTextField.setText(Double.toString(Math.round(MIDIBeast.chordPart.getPhrase(0).getEndTime())));

    }



    public void addBassRawRules() {

        ArrayList<RepresentativeBassRules.Section> sections = repBassRules.getSections();
