    static int countToBeFinalized() {

        int count = 0;

        VM_FinalizerListElement le = finalize_head;

        while (le != null) {

            count++;

            if (COUNT_BY_TYPES) {

                VM_Type type = VM_Magic.getObjectType(le.pointer);

                type.liveCount++;

            }

            le = le.next;

        }

        return count;

    }
