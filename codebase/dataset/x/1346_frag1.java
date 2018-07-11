    private void readChord(int strings, TGBeat beat) throws IOException {

        TGChord chord = getFactory().newChord(strings);

        chord.setName(readStringByte(0));

        this.skip(1);

        if (readInt() < 12) {

            skip(32);

        }

        chord.setFirstFret(readInt());

        if (chord.getFirstFret() != 0) {

            for (int i = 0; i < 6; i++) {

                int fret = readInt();

                if (i < chord.countStrings()) {

                    chord.addFretValue(i, fret);

                }

            }

        }

        if (chord.countNotes() > 0) {

            beat.setChord(chord);

        }

    }
