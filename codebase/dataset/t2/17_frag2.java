    public static VM_ThreadIOWaitData ioWaitWrite(int fd, double totalWaitTime) {

        long maxWaitCycle = getMaxWaitCycle(totalWaitTime);

        VM_ThreadIOWaitData waitData = new VM_ThreadIOWaitData(maxWaitCycle);

        waitData.writeFds = new int[] { fd };

        if (noIoWait) {

            waitData.markAllAsReady();

        } else {

            VM_Thread.ioWaitImpl(waitData);

        }

        return waitData;

    }
