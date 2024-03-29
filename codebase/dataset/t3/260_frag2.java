    private void cleanup() {

        if (myActiveConnection != null) {

            try {

                myActiveConnection.disconnect();

            } catch (Exception e) {

                log.log(Level.SEVERE, "Unable to disconnect.", e);

            }

            myActiveConnection = null;

        }

        if (responseStream != null) {

            try {

                responseStream.close();

            } catch (IOException e) {

                log.log(Level.SEVERE, "Unable to close response stream.", e);

            }

            responseStream = null;

        }

    }
