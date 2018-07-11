        try {

            VM_Field field = bmap.findVMField("com.ibm.JikesRVM.VM_Thread", "J2NLockFailureCount");

            temp = Integer.toString(mem.read(threadPointer + field.getOffset()));

            result += blanks.substring(1, blanks.length() - temp.length()) + temp;

        } catch (BmapNotFoundException e) {

            throw new Exception("cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?");

        }

        try {

            VM_Field field = bmap.findVMField("com.ibm.JikesRVM.RVM_Thread", "J2NTotalYieldDuration");

            temp = Integer.toString(mem.read(threadPointer + field.getOffset()));

            result += blanks.substring(1, blanks.length() - temp.length()) + temp;

        } catch (BmapNotFoundException e) {

            throw new Exception("cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?");

        }

        try {

            VM_Field field = bmap.findVMField("com.ibm.JikesRVM.VM_Thread", "J2NTotalLockDuration");

            temp = Integer.toString(mem.read(threadPointer + field.getOffset()));

            result += blanks.substring(1, blanks.length() - temp.length()) + temp;

        } catch (BmapNotFoundException e) {

            throw new Exception("cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?");

        }

        return result;
