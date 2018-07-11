            bpset.setBreakpoint(bpSaved);

        }

        stillrunning = continueCheckingForSignal(thread, printMode, allThreads);

        return stillrunning;

    }



    /**

   * Continue to the return address from the last branch and link

   * (unless stopped by another breakpoint in between)

   * @param thread the thread to control

   * @param printMode flag to display the current java source line or

   *                  machine instruction

   * @return true if the process is still running, false if it has exited

   * @exception

   * @see

   */

    public boolean pcontinueToReturn(int thread, int printMode) {

        int addr = reg.hardwareIP();

        boolean stillrunning;

        breakpoint bpSaved = bpset.lookup(addr);

        threadstep.setLinkBreakpoint(thread);

        bpset.clearBreakpoint(bpSaved);
