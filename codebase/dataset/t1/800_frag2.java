    public static int indexOf(byte[] array, byte valueToFind, int startIndex) {

        if (array == null) {

            return INDEX_NOT_FOUND;

        }

        if (startIndex < 0) {

            startIndex = 0;

        }

        for (int i = startIndex; i < array.length; i++) {

            if (valueToFind == array[i]) {

                return i;

            }

        }

        return INDEX_NOT_FOUND;

    }
