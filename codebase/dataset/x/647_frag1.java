    public void disconnect() {

        responseCode = -1;

        if (pe != null) {

            ProgressData.pdata.unregister(pe);

        }

        if (http != null) {

            if (inputStream != null) {

                HttpClient hc = http;

                boolean ka = hc.isKeepingAlive();

                try {

                    inputStream.close();

                } catch (IOException ioe) {

                }

                if (ka) {

                    hc.closeIdleConnection();

                }

            } else {

                http.closeServer();

            }

            http = null;

            connected = false;

        }

    }
