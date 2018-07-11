    private Socket newSocket(String host, int port) throws IOException, GeneralSecurityException {

        if (cipherSuites != null) {

            return tls.getSocketFactory(cipherSuites).createSocket(host, port);

        } else {

            return new Socket(host, port);

        }

    }
