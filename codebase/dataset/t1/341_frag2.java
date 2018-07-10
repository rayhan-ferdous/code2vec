    public static String nullSafeToString(Object[] array) {

        if (array == null) {

            return NULL_STRING;

        }

        int length = array.length;

        if (length == 0) {

            return EMPTY_ARRAY;

        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < length; i++) {

            if (i == 0) {
