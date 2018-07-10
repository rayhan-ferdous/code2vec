    public JDPServer(int initialPort) {

        port = initialPort;

        boolean socketOpened = false;

        while (!socketOpened) {

            try {

                serverSocket = new ServerSocket(port);

                System.out.println("JDPServer running on port " + port);

                socketOpened = true;

            } catch (IOException e) {

                if (++port > 65536) {

                    System.err.println("No available port");

                    System.exit(1);

                }

            }

        }

    }
