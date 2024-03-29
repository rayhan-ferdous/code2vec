    void dumpWaitDescription(VM_Thread thread) throws VM_PragmaInterruptible {

        WaitDataDowncaster downcaster = new WaitDataDowncaster();

        thread.waitData.accept(downcaster);

        VM_ThreadIOWaitData waitData = downcaster.waitData;

        if (VM.VerifyAssertions) VM._assert(waitData == thread.waitData);

        VM.sysWrite("(R");

        dumpFds(waitData.readFds);

        VM.sysWrite(";W");

        dumpFds(waitData.writeFds);

        VM.sysWrite(";E");

        dumpFds(waitData.exceptFds);

        VM.sysWrite(')');

    }
