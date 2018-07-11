    public boolean pcontinue(int thread, int printMode, boolean allThreads) {

        int addr = reg.hardwareIP();

        boolean over_branch = false;

        boolean stillrunning;

        boolean skip_prolog = false;

        breakpoint bpSaved = bpset.lookup(addr);

        if (bpSaved != null) {

            System.out.println("pcontinue: saving current breakpoint " + VM.intAsHexString(addr));

            threadstep.setStepBreakpoint(thread, over_branch, skip_prolog);

            bpset.clearBreakpoint(bpSaved);

            stillrunning = continueCheckingForSignal(thread, PRINTNONE, false);

            threadstep.clearStepBreakpoint(thread);

            bpset.setBreakpoint(bpSaved);

        }

        stillrunning = continueCheckingForSignal(thread, printMode, allThreads);

        return stillrunning;

    }
