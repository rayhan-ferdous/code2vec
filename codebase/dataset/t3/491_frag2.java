    public void disconnect() {

        try {

            dataIn.close();

        } catch (IOException e) {

        }

        try {

            dataOut.close();

        } catch (IOException e) {

        }

    }
