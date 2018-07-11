        chain.addLast(dh3);

        IServer server = new Server(chain);

        server.start();

        IBlockingConnection con = new BlockingConnection("localhost", server.getLocalPort());

        con.write("test\r\n");

        Assert.assertEquals("test", con.readStringByDelimiter("\r\n"));

        QAUtil.sleep(1000);
