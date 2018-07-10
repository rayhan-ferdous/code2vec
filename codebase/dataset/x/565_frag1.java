    public void disconnect() {

        connected = false;

        try {

            fromClient.close();

        } catch (IOException ex) {

            Server.log(ex);

        }

        fromClient = null;

        try {

            toClient.close();

        } catch (IOException ex) {

            Server.log(ex);

        }

        toClient = null;

        disconnectClient = true;

    }
