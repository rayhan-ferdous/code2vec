    public void n2sort() {

        for (int i = 0; i < getRowCount(); i++) {

            for (int j = i + 1; j < getRowCount(); j++) {

                if (compare(indexes[i], indexes[j]) == -1) {

                    swap(i, j);

                }

            }

        }

    }
