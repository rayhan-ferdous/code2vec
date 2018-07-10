            decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

            audioInputStream = AudioSystem.getAudioInputStream(decodedFormat, ais);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
