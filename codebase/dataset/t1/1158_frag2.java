    public static boolean isSorted(int[] array, int offset, int length) {

        int v = Integer.MIN_VALUE;

        for (int i = 0; i < length; i++) {

            int w = array[i + offset];

            if (w < v) return false;

            v = w;

        }

        return true;

    }
