                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);

                ais = AudioSystem.getAudioInputStream(targetFormat, ais);
