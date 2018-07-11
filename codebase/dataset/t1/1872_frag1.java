    public Message notimplMessage(Message query) {

        Header header = query.getHeader();

        Message response = new Message();

        response.setHeader(header);

        for (int i = 0; i < 4; i++) response.removeAllRecords(i);

        header.setRcode(Rcode.NOTIMPL);

        return response;

    }



    public void serveTCP(short port) {

        try {

            ServerSocket sock = new ServerSocket(port);

            while (true) {

                Socket s = sock.accept();

                int inLength;

                DataInputStream dataIn;

                DataOutputStream dataOut;

                byte[] in;

                try {

                    InputStream is = s.getInputStream();

                    dataIn = new DataInputStream(is);

                    inLength = dataIn.readUnsignedShort();

                    in = new byte[inLength];

                    dataIn.readFully(in);

                } catch (InterruptedIOException e) {

                    s.close();

                    continue;

                }

                Message query, response;

                try {
