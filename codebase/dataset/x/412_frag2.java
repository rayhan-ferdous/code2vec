                @Override

                public void trigger() {

                    try {

                        channel.sendMessage(copy);

                    } catch (InvalidMidiDataException nothingWeCanDoAboutIt) {

                    }

                }
