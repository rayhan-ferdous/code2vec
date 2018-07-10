    private void connect() {

        try {

            System.out.println("- Connecting to DAQ:");

            startDaqConnections();

            connectToDaqControl();

            connected = true;

        } catch (Throwable t) {

            t.printStackTrace();

        }

    }
