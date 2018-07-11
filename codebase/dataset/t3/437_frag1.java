    public int getVMThreadForIndex(int index) throws Exception {

        if (index == 0) return 0;

        try {

            VM_Field field = bmap.findVMField("VM_Scheduler", "threads");

            int address = mem.readTOC(field.getOffset());

            int arraySize = mem.read(address + VM.ARRAY_LENGTH_OFFSET);

            return mem.read(address + index * 4);

        } catch (BmapNotFoundException e) {

            throw new Exception("cannot find VM_Scheduler.threads, has VM_Scheduler.java been changed?");

        }

    }
