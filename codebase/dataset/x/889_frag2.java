    @Test

    public void testNonBlockingClientSide() throws Exception {

        IServer server = new Server(new ServerHandler());

        server.start();

        INonBlockingConnection connection = new NonBlockingConnection("localhost", server.getLocalPort());

        connection.setFlushmode(FlushMode.ASYNC);

        String request = "dsfdsdsffds";

        connection.write(request + DELIMITER);

        QAUtil.sleep(1000);

        String response = connection.readStringByDelimiter(DELIMITER);

        Assert.assertEquals(request, response);

        connection.close();

        server.close();

    }
