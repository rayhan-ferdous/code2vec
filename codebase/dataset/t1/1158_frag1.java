    public static boolean isSorted(long[] array, int offset, int length) {

        long v = Long.MIN_VALUE;

        for (int i = 0; i < length; i++) {

            long w = array[i + offset];

            if (w < v) return false;

            v = w;

        }

        return true;

    }
