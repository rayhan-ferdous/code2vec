    public static String join(Object[] array, String delimeter) {

        StringBuilder sb = new StringBuilder();

        if (array != null && array.length > 0) {

            sb.append(array[0]);

            for (int i = 1; i < array.length; i++) {

                sb.append(delimeter);

                sb.append(array[i]);

            }

        }

        return sb.toString();

    }
