    public void serveUDP(InetAddress addr, short port) {

        try {

            DatagramSocket sock = new DatagramSocket(port, addr);

            final short udpLength = 512;

            byte[] in = new byte[udpLength];

            DatagramPacket indp = new DatagramPacket(in, in.length);

            DatagramPacket outdp = null;

            while (true) {

                indp.setLength(in.length);

                try {

                    sock.receive(indp);

                } catch (InterruptedIOException e) {

                    continue;

                }

                Message query;

                byte[] response = null;

                try {

                    query = new Message(in);

                    response = generateReply(query, in, indp.getLength(), null);
