            VM_Field field = bmap.findVMField("VM_Thread", "J2NTotalLockDuration");

            temp = Integer.toString(mem.read(threadPointer + field.getOffset()));

            result += blanks.substring(1, blanks.length() - temp.length()) + temp;

        } catch (BmapNotFoundException e) {

            throw new Exception("cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?");

        }
