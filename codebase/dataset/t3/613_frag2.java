    public static boolean probFormatForPlayback(AudioFormat format) {

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {

            return false;

        }

        try {

            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            line.open(format, line.getBufferSize());

            line.close();

            return true;

        } catch (Throwable t) {

        }

        return false;

    }
