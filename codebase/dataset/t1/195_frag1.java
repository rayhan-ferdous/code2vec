    public static int nullSafeHashCode(boolean[] array) {

        if (array == null) {

            return 0;

        }

        int hash = INITIAL_HASH;

        int arraySize = array.length;

        for (int i = 0; i < arraySize; i++) {

            hash = MULTIPLIER * hash + hashCode(array[i]);

        }

        return hash;

    }
