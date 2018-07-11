        if (stillrunning && bpSaved != null) bpset.setBreakpoint(bpSaved);

        return stillrunning;

    }



    /**

   * Common code for pstep, pstepOverBranch, pcontinue

   * @param thread the thread to control

   * @param printMode flag to display the current java source line or

   *                  machine instruction

   * @return  true if if the process is still running, false if it has exited

   * @exception

   * @see

   */

    private boolean continueCheckingForSignal(int thread, int printMode, boolean allThreads) {

        if (lastSignal[thread] == 0) mcontinue(0); else {

            int sig = lastSignal[thread];

            lastSignal[thread] = 0;

            mcontinue(sig);

        }

        try {

            pwait(allThreads);
