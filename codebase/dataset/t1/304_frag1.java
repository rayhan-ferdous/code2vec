    public void serveUDP(InetAddress addr, int port) {

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

                    if (response == null) continue;

                } catch (IOException e) {

                    response = formerrMessage(in);

                }

                if (outdp == null) outdp = new DatagramPacket(response, response.length, indp.getAddress(), indp.getPort()); else {

                    outdp.setData(response);

                    outdp.setLength(response.length);

                    outdp.setAddress(indp.getAddress());

                    outdp.setPort(indp.getPort());

                }

                sock.send(outdp);

            }

        } catch (IOException e) {

            System.out.println("serveUDP(" + addrport(addr, port) + "): " + e);

        }

    }
