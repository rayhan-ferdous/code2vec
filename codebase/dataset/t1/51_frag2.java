    private void rawplay(AudioFormat trgFormat, AudioInputStream ain, float volume) throws IOException, LineUnavailableException {

        byte[] data = new byte[8192];

        try {

            clip = getLine(ain, trgFormat);

            if (clip == null) {

                return;

            }

            Control.Type vol1 = FloatControl.Type.VOLUME, vol2 = FloatControl.Type.MASTER_GAIN;

            FloatControl c = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float min = c.getMinimum();

            float v = volume * (c.getMaximum() - min) / 100f + min;

            if (this.clip.isControlSupported(vol1)) {

                FloatControl volumeControl = (FloatControl) this.clip.getControl(vol1);

                volumeControl.setValue(v);

            } else if (this.clip.isControlSupported(vol2)) {

                FloatControl gainControl = (FloatControl) this.clip.getControl(vol2);

                gainControl.setValue(v);

            }

            clip.start();

            int nBytesRead = 0;

            while (isRunning && (nBytesRead != -1)) {

                nBytesRead = ain.read(data, 0, data.length);

                if (nBytesRead != -1) {

                    clip.write(data, 0, nBytesRead);

                }

            }

        } finally {

            clip.drain();

            clip.stop();

            clip.close();

            ain.close();

        }

    }
