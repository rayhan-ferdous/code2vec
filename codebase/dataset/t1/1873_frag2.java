    public static final int find(int[] a, int key, int begin, int end) {

        for (int i = begin; i < end; i++) {

            if (a[i] == key) {

                return i;

            }

        }

        return -1;

    }
