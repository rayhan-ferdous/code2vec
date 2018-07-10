    private String wakeupQueueToString(int queuePointer) {

        String result = "";

        int count = 0;

        int fieldOffset;

        int thisProxyPointer, thisThreadPointer;

        VM_Field field;

        try {

            field = bmap.findVMField("VM_ProxyWakeupQueue", "head");

            thisProxyPointer = mem.read(queuePointer + field.getOffset());

            while (thisProxyPointer != 0) {

                field = bmap.findVMField("VM_Proxy", "thread");

                thisThreadPointer = mem.read(thisProxyPointer + field.getOffset());

                if (thisThreadPointer != 0) {

                    result += "   " + threadToString(thisThreadPointer) + "\n";

                    count++;

                }

                field = bmap.findVMField("VM_Proxy", "wakeupNext");

                thisProxyPointer = mem.read(thisProxyPointer + field.getOffset());

            }

        } catch (BmapNotFoundException e) {

            return "ERROR: cannot find VM_ThreadQueue.head or tail, has VM_ThreadQueue been changed?";

        }

        String heading = "";

        heading += "  ID  VM_Thread   top stack frame\n";

        heading += "  -- -----------  -----------------\n";

        return "Threads in queue:  " + count + "\n" + heading + result;

    }
