    public void checkModel() {

        if (indexes.length != model.getRowCount()) {

            System.err.println("Sorter not informed of a change in model.");

        }

    }



    /**

	 * Sort the table.

	 * @param sender, the class using this method.

	 */

    public void sort(Object sender) {

        checkModel();

        compares = 0;

        shuttlesort((int[]) indexes.clone(), indexes, 0, indexes.length);

    }



    /**

	 * Sort the table.

	 */

    public void n2sort() {

        for (int i = 0; i < getRowCount(); i++) {

            for (int j = i + 1; j < getRowCount(); j++) {

                if (compare(indexes[i], indexes[j]) == -1) {

                    swap(i, j);

                }

            }

        }

    }



    /**

	 * Sorts. For more detail read the source code.

	 */

    public void shuttlesort(int from[], int to[], int low, int high) {

        if (high - low < 2) {

            return;

        }

        int middle = (low + high) / 2;

        shuttlesort(to, from, low, middle);

        shuttlesort(to, from, middle, high);

        int p = low;

        int q = middle;

        if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {

            for (int i = low; i < high; i++) {

                to[i] = from[i];

            }

            return;

        }

        for (int i = low; i < high; i++) {

            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {

                to[i] = from[p++];

            } else {

                to[i] = from[q++];

            }

        }

    }



    /**

	 * Swap the elements at the given indexes.

	 * @param i, the first index to swap.

	 * @param j, the second index to swap.

	 */

    public void swap(int i, int j) {

        int tmp = indexes[i];

        indexes[i] = indexes[j];

        indexes[j] = tmp;
