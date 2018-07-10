    public void close() {

        if (!aborting) {

            synchronized (this) {

                aborting = true;

            }

            synchronized (sendMessages) {

                sendMessages.notify();

            }

            if (out != null) {

                try {

                    out.close();

                    synchronized (this) {

                        out = null;

                    }

                } catch (IOException e) {

                }

            }

            if (in != null) {

                try {

                    in.close();

                    synchronized (this) {

                        in = null;

                    }

                } catch (IOException e) {

                }

            }

            if (connection != null) {

                try {

                    connection.close();

                    synchronized (this) {

                        connection = null;

                    }

                } catch (IOException e) {

                }

            }

        }

    }
