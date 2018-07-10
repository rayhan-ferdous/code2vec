    public int compare(int row1, int row2) {

        compares++;

        for (int level = 0; level < sortingColumns.size(); level++) {

            Integer column = (Integer) sortingColumns.elementAt(level);

            int result = compareRowsByColumn(row1, row2, column.intValue());

            if (result != 0) return ascending ? result : -result;

        }

        return 0;

    }



    public void swap(int i, int j) {
