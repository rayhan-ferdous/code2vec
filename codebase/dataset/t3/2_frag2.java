            public void run() {

                File file1 = new File(audioFile);

                byte[] buf = null;

                try {

                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file1));

                    while (bis.available() < file1.length()) System.out.print("");

                    buf = new byte[bis.available()];

                    bis.read(buf, 0, buf.length);

                    bis.close();

                } catch (Exception ex1) {

                    mbv.showErrorDialog(ex1);

                }

                try {

                    SocketObject so;

                    if (buf != null) {

                        so = new SocketObject(null, "SendToAddress", "", socket.getLocalAddress().getHostAddress(), address, buf);

                    } else {

                        so = new SocketObject(null, "FileNotFound", "", socket.getLocalAddress().getHostAddress(), address, null);

                    }

                    System.out.println("destination: " + so.getDestination());

                    oos.writeObject(so);

                    oos.flush();

                } catch (IOException ex) {

                    mbv.showErrorDialog(ex);

                }

            }
