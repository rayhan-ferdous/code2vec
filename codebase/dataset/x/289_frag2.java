                @Override

                public void run() {

                    try {

                        OutputStream os = pipe.getOutputStream();

                        representation.write(os);

                        os.write(-1);

                        os.close();

                    } catch (IOException ioe) {

                        ioe.printStackTrace();

                    }

                }
