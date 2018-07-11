            public void run() {

                try {

                    lock.writeLock().lock();

                    threadAssertFalse(lock.hasWaiters(c));

                    threadAssertEquals(0, lock.getWaitQueueLength(c));

                    c.await();

                    lock.writeLock().unlock();

                } catch (InterruptedException e) {

                    threadUnexpectedException();

                }

            }
