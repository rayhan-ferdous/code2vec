    public boolean pstepOverBranch(int thread) {

        int addr = reg.hardwareIP();

        boolean over_branch = true;

        boolean stillrunning;

        boolean skip_prolog = false;

        breakpoint bpSaved = bpset.lookup(addr);

        threadstep.setStepBreakpoint(thread, over_branch, skip_prolog);

        if (bpSaved != null) bpset.clearBreakpoint(bpSaved);

        stillrunning = continueCheckingForSignal(thread, PRINTASSEMBLY, false);

        threadstep.clearStepBreakpoint(thread);

        if (stillrunning && bpSaved != null) bpset.setBreakpoint(bpSaved);

        return stillrunning;

    }
