        ResourcePool.LogMessage(Thread.currentThread(), ResourcePool.DEBUG_MESSAGE, "- Starting threads");

        synchronized (this) {

            for (Object o : this.threads) {

                ((WorkerThread) o).thread.start();

            }

        }

        ResourcePool.LogMessage(Thread.currentThread(), ResourcePool.DEBUG_MESSAGE, "Threads initialized");
