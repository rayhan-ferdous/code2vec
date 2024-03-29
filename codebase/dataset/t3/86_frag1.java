    public void close() {

        if (receiver != null) {

            receiver.close();

        }

        if (transmitter != null) {

            transmitter.close();

        }

        if (synthesizer != null) {

            synthesizer.close();

        }

        if (sequencer != null) {

            sequencer.close();

        }

        for (Receiver r : receivers) {

            r.close();

        }

        opened = false;

    }
