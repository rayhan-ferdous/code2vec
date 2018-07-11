        public void run() {

            try {

                while (running) {

                    byte[] buffer = new byte[MAX_UDP_SIZE];

                    DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

                    socket.receive(dp);

                    receivedMessage(dp.getData());

                }

                if (channel != null) {

                    channel.disconnect();

                }

            } catch (IOException e) {

                if (running) {

                    logger.error("IOException ignored", e);

                }

            }

        }
