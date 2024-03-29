    public void serveUDP(short port) {

        try {

            DatagramSocket sock = new DatagramSocket(port);

            while (true) {

                short udpLength = 512;

                DatagramPacket dp = new DatagramPacket(new byte[512], 512);

                try {

                    sock.receive(dp);

                } catch (InterruptedIOException e) {

                    continue;

                }

                byte[] in = new byte[dp.getLength()];

                System.arraycopy(dp.getData(), 0, in, 0, in.length);

                Message query, response;

                try {

                    query = new Message(in);

                    response = generateReply(query, in, null);

                    if (response == null) continue;

                } catch (IOException e) {

                    response = formerrMessage(in);

                }

                byte[] out = response.toWire();

                dp = new DatagramPacket(out, out.length, dp.getAddress(), dp.getPort());

                sock.send(dp);

            }

        } catch (IOException e) {

            System.out.println("serveUDP: " + e);

        }

    }
