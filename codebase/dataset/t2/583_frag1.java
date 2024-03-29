    public SortedSet tailSet(Object fromElement) {

        boolean wasInterrupted = beforeRead();

        try {

            return new SyncSortedSet(baseSortedSet().tailSet(fromElement), rd_, wr_);

        } finally {

            afterRead(wasInterrupted);

        }

    }
