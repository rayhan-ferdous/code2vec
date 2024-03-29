    public static String nullSafeToString(char[] array) {

        if (array == null) {

            return NULL_STRING;

        }

        int length = array.length;

        if (length == 0) {

            return EMPTY_ARRAY;

        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {

            if (i == 0) {

                sb.append(ARRAY_START);

            } else {

                sb.append(ARRAY_ELEMENT_SEPARATOR);

            }

            sb.append("'").append(array[i]).append("'");

        }

        sb.append(ARRAY_END);

        return sb.toString();

    }
