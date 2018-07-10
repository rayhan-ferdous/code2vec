        public void run() {

            while (true) {

                try {

                    postToYapbam(errorsQueue.take());

                } catch (InterruptedException e) {

                } catch (Throwable e) {

                    e.printStackTrace();

                }

            }

        }
