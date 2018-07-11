    public int available() {

        int avail = -1;

        if (encodedAudioInputStream != null) {

            try {

                avail = encodedAudioInputStream.available();

            } catch (IOException e) {

            }

        }

        return avail;

    }
