            System.out.println("TCPclient(" + addrport(s.getLocalAddress(), s.getLocalPort()) + "): " + e);

        } finally {

            try {

                s.close();

            } catch (IOException e) {

            }

        }

    }



    public void serveTCP(InetAddress addr, int port) {

        try {

            ServerSocket sock = new ServerSocket(port, 128, addr);

            while (true) {

                final Socket s = sock.accept();

                Thread t;

                t = new Thread(new Runnable() {



                    public void run() {

                        TCPclient(s);

                    }

                });

                t.start();

            }

        } catch (IOException e) {

            System.out.println("serveTCP(" + addrport(addr, port) + "): " + e);
