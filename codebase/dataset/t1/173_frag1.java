    public static int nullSafeHashCode(char[] array) {

        if (array == null) {

            return 0;

        }

        int hash = INITIAL_HASH;

        int arraySize = array.length;

        for (int i = 0; i < arraySize; i++) {

            hash = MULTIPLIER * hash + array[i];

        }

        return hash;

    }
