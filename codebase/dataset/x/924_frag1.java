    public void writeAudioData(final OutputStream out, final AudioFileFormat.Type type) throws IOException, AudioException {

        if (out == null) {

            return;

        }

        try {

            AudioInputStream ais = AudioSystem.getAudioInputStream(getAudioData());

            AudioSystem.write(ais, type, out);

        } catch (UnsupportedAudioFileException uafe) {

            throw new AudioException(uafe);

        }

    }
