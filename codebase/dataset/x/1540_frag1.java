    public void halt() {

        if (_serverSock != null) {

            try {

                _halted = true;

                _serverSock.close();

                _serverSock = null;

            } catch (IOException e) {

            }

        }

    }
