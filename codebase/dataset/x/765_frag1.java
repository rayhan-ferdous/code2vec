    public void stop() {

        if (thread != null) {

            thread.interrupt();

        }

        thread = null;

    }
