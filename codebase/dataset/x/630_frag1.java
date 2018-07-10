        if (indexes.length != filteredModel.getRowCount()) {

            throw new IllegalStateException("Sorter not informed of a change in model.");

        }

    }



    private void doShuttleSort(int from[], int to[], int low, int high) {

        if (high - low < 2) {

            return;

        }

        int middle = (low + high) / 2;

        doShuttleSort(to, from, low, middle);
