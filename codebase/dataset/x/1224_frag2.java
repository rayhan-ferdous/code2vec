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

	 * This is a home-grown implementation which we have not had time to

	 * research - it may perform poorly in some circumstances. It requires twice

	 * the space of an in-place algorithm and makes NlogN assigments shuttling

	 * the values between the two arrays. The number of compares appears to vary

	 * between N-1 and NlogN depending on the initial order but the main reason

	 * for using it here is that, unlike qsort, it is stable.

	 * 

	 * @param from

	 * @param to

	 * @param low

	 * @param high

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

	 * 

	 * @param i

	 * @param j

	 */

    public void swap(int i, int j) {
