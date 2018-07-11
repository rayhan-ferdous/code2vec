    void exit(boolean kill) {

        if (stopListener) return;

        instance = null;

        stopListener = true;

        monitor.setEventOptions(DebugInterface.DBG_PROGRAM_EXIT, DebugInterface.DBG_EVENT_IGNORE);

        monitor.suspendProgram();

        this.interrupt();

        eventThread.interrupt();

        synchronized (packetQueue) {

            packetQueue.notify();

        }

        synchronized (waitingQueue) {

            waitingQueue.notify();

        }

        while (!finished) {

            try {

                Thread.sleep(50);

            } catch (InterruptedException e) {

            }

        }

        EventRequest.clearAll();

        Breakpoint.removeAllBreakpoints();

        if (conn != null) {

            conn.close();

        }

        monitor.setEventOptions(DebugInterface.DBG_PROGRAM_EXIT, DebugInterface.DBG_EVENT_DISABLE);

        monitor.resumeProgram();

        if (kill) {

            System.exit(0);

        }

    }
