        public void run() {

            try {

                lock.writeLock().lockInterruptibly();

                threadShouldThrow();

            } catch (InterruptedException success) {

            }

        }
