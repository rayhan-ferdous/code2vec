                public void run() {

                    while (true) {

                        try {

                            ServerSocket serverSocket = new ServerSocket(3000);

                            (new SPOTSocketHandler(serverSocket.accept())).start();

                            serverSocket.close();

                        } catch (IOException ex) {

                            ex.printStackTrace();

                        }

                    }

                }
