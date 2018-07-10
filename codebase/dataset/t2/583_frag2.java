    public SortedMap tailMap(Object fromElement) {

        boolean wasInterrupted = beforeRead();

        try {

            return new SyncSortedMap(baseSortedMap().tailMap(fromElement), rd_, wr_);

        } finally {

            afterRead(wasInterrupted);

        }

    }
