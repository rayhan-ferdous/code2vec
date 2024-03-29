        shuttlesort((int[]) indexes.clone(), indexes, 0, indexes.length);

    }



    public void n2sort() {

        for (int i = 0; i < getRowCount(); i++) {

            for (int j = i + 1; j < getRowCount(); j++) {

                if (compare(indexes[i], indexes[j]) == -1) {

                    swap(i, j);

                }

            }

        }

    }



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



    public void swap(int i, int j) {

        int tmp = indexes[i];

        indexes[i] = indexes[j];

        indexes[j] = tmp;

    }



    public Object getValueAt(int aRow, int aColumn) {
