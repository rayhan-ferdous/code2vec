    public void run() {

        while (true) {

            try {

                getRemoteContent();

                Thread.currentThread().sleep(5000);

            } catch (InterruptedException e) {

                ReOSApplication.log.Write(e);

            }

        }

    }
