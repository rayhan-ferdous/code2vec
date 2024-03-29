    public int getIndexForVMThread(int threadPointer) throws Exception {

        if (threadPointer == 0) return 0;

        try {

            VM_Field field = bmap.findVMField("VM_Thread", "threadSlot");

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

   * 

   * @return  

   * @exception

   * @see VM_Thread, VM_Scheduler

   */

    public String listAllThreads(boolean byClassName) {
