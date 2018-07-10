    public boolean pcontinueToReturn(int thread, int printMode) {

        int addr = reg.hardwareIP();

        boolean stillrunning;

        breakpoint bpSaved = bpset.lookup(addr);

        threadstep.setLinkBreakpoint(thread);

        bpset.clearBreakpoint(bpSaved);

        stillrunning = continueCheckingForSignal(thread, printMode, false);

        threadstep.clearStepBreakpoint(thread);

        if (stillrunning && bpSaved != null) bpset.setBreakpoint(bpSaved);

        return stillrunning;

    }
