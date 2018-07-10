    public int getIndexForVMThread(int threadPointer) throws Exception {

        if (threadPointer == 0) return 0;

        try {

            VM_Field field = bmap.findVMField("com.ibm.JikesRVM.VM_Thread", "threadSlot");

            int address = mem.read(threadPointer + field.getOffset());

            return address;

        } catch (BmapNotFoundException e) {

            throw new Exception("cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?");

        }

    }



    public boolean isValidVMThread(int threadPointer) throws Exception {

        int vmthreads[] = getAllThreads();

        for (int i = 0; i < vmthreads.length; i++) {

            if (threadPointer == vmthreads[i]) {

                return true;

            }

        }

        return false;

    }



    /**

   * Returns a pointer to the current active thread.

   * @return a pointer to the current active thread.

   */

    public int activeThread() {

        return reg.hardwareTP();

    }



    /**

   * Find the name of a thread

   * @return then name of the thread pointer to by <code>threadPointer</code>

   */

    public String threadName(int threadPointer) {
