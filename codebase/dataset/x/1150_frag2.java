    private void playMIDIfileActionPerformed(java.awt.event.ActionEvent evt) {

        try {

            jmScore = midiImport.getScore();

            setJmVolume();

            if (jmSynth != null) {

                jmSynth.stop();

            }

            jmSynth = new jm.midi.MidiSynth();

            jmSynth.play(jmScore);

        } catch (InvalidMidiDataException e) {

        }

    }
