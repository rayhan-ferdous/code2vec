    public void run() {

        for (; ; ) {

            Packet packet = _clientqueue.getNextPacket();

            try {

                _ssh_out.writePacket(packet);

            } catch (IOException e) {

                e.printStackTrace();

                System.exit(0);

            }

        }

    }
