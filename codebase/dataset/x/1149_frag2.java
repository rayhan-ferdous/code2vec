    private int handleSynthesisRequest() {

        try {

            String sampleRateLine = reader.readLine();

            int sampleRate = Integer.parseInt(sampleRateLine);

            if (sampleRate == 8000) {

                voice = server.get8kVoice();

            } else if (sampleRate == 16000) {

                voice = server.get16kVoice();

            } else {

                sendLine("-2");

                return INVALID_SAMPLE_RATE;

            }

            String text = reader.readLine();

            voice.setAudioPlayer(socketAudioPlayer);

            voice.speak(text);

            sendLine("-1");

        } catch (IOException ioe) {

            ioe.printStackTrace();

        }

        return 0;

    }
