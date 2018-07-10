    public int compare(int row1, int row2) {

        this.compares++;

        for (int level = 0; level < this.sortingColumns.size(); level++) {

            Integer column = this.sortingColumns.elementAt(level);

            int result = compareRowsByColumn(row1, row2, column.intValue());

            if (result != 0) {

                return this.ascending ? result : -result;

            }

        }

        return 0;

    }
