    public void TCPclient(Socket s) {

        try {

            int inLength;

            DataInputStream dataIn;

            DataOutputStream dataOut;

            byte[] in;

            InputStream is = s.getInputStream();

            dataIn = new DataInputStream(is);

            inLength = dataIn.readUnsignedShort();

            in = new byte[inLength];

            dataIn.readFully(in);

            Message query;

            byte[] response = null;

            try {

                query = new Message(in);

                response = generateReply(query, in, in.length, s);

                if (response == null) return;

            } catch (IOException e) {

                response = formerrMessage(in);

            }

            dataOut = new DataOutputStream(s.getOutputStream());

            dataOut.writeShort(response.length);

            dataOut.write(response);

        } catch (IOException e) {

            String addrString;

            System.out.println("TCPclient(" + s.getLocalAddress().getHostAddress() + "#" + s.getLocalPort() + "): " + e);

        } finally {

            try {

                s.close();

            } catch (IOException e) {

            }

        }

    }
