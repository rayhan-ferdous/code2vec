    protected void startMaintThread() {

        if (maintThread != null) {

            if (maintThread.isRunning()) {

                log.error("main thread already running");

            } else {

                maintThread.start();

            }

        } else {

            maintThread = new MaintThread(this);

            maintThread.setInterval(this.maintSleep);

            maintThread.start();

        }

    }
