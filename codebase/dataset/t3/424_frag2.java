        public synchronized void run() {

            assert (runThread == Thread.currentThread());

            while (true) {

                try {

                    wait();

                } catch (InterruptedException e1) {

                    while (!buildThumbNail()) Thread.interrupted();

                }

            }

        }
