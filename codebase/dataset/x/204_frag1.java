    public static void setDevice(String deviceName) throws MidiUnavailableException {

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        for (int i = 0; i < infos.length; i++) {

            String infoString = infos[i].getName();

            System.out.println(infoString);

            if (infoString.startsWith(deviceName)) {

                MidiDevice device = MidiSystem.getMidiDevice(infos[i]);

                if (!device.isOpen()) {

                    try {

                        device.open();

                    } catch (Exception e) {

                        continue;

                    }

                }

                midiDevice = device;

                if (device instanceof Synthesizer) {

                    synthesizer = (Synthesizer) device;

                }

                break;

            }

        }

    }
