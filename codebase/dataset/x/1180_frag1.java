        @Override

        public void close() throws IOException {

            synchronized (channel) {

                if (channel.isOpen()) {

                    channel.close();

                } else {

                    super.close();

                }

                channel.status = SocketChannelImpl.SOCKET_STATUS_CLOSED;

            }

        }
