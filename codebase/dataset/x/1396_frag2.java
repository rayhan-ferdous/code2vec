    public void disconnectWarrior() {

        while (this.writingFileLock.intValue() > 0) {

            try {

                sleep(1000);

            } catch (InterruptedException ex) {

            }

        }

        if (InetAddressEngine.getInstance() != null) InetAddressEngine.getInstance().terminate();

        this.disconnect();

    }
