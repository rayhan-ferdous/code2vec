    public void stop() {

        bContinue = false;

        try {

            if (null != ois) {

                ois.close();

            }

            if (thread != Thread.currentThread()) {

                thread.join();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
