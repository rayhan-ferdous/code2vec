    @Inline

    final Address alloc() {

        Address rtn = rps.acquire(PAGES_PER_BUFFER);

        if (rtn.isZero()) {

            Space.printUsageMB();

            VM.assertions.fail("Failed to allocate space for queue.  Is metadata virtual memory exhausted?");

        }

        if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(rtn.EQ(bufferStart(rtn)));

        return rtn;

    }
