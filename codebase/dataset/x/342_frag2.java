    protected void internalStop() {

        outerRun = false;

        try {

            mSocket.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
