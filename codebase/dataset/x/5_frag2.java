        theSocket.close();

        theSocket2.close();

        theSocket = new Socket();

        try {

            theSocket.bind(new UnsupportedSocketAddress());

            fail("No exception when binding using unsupported SocketAddress subclass");

        } catch (IllegalArgumentException ex) {
