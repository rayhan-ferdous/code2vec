        ResourcePool.LogMessage(Thread.currentThread(), ResourcePool.DEBUG_MESSAGE, "- Registering queues");

        for (Object o : this.threads) {

            ((WorkerThread) o).step.initializeQueues();

        }

        ResourcePool.LogMessage(Thread.currentThread(), ResourcePool.DEBUG_MESSAGE, "- Initializing core managers");
