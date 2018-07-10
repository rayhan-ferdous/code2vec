    public boolean read() throws IOException {

        if (!isAudioStream()) {

            while (true) {

                if (!processRead()) {

                    break;

                }

            }

            return true;

        }

        return false;

    }
