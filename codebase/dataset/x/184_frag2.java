    public void stop() {

        if (conn.isListening()) {

            try {

                Thread.sleep(shutdownDelay);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            conn.unbind();

        }

    }
