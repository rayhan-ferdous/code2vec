    private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {

        byte[] data = new byte[4096];

        SourceDataLine line = getLine(targetFormat);

        if (line != null) {

            line.start();

            int nBytesRead = 0, nBytesWritten = 0;

            while (nBytesRead != -1) {

                nBytesRead = din.read(data, 0, data.length);

                if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);

            }

            line.drain();

            line.stop();

            line.close();

            din.close();

        }

    }
