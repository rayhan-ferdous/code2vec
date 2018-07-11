    public static String arrayToString(Object[] arr) {

        StringBuilder sb = new StringBuilder();

        int len = arr.length;

        for (int i = 0; i < len; i++) {

            sb.append(arr[i]);

            if (i != len - 1) {

                sb.append(",");

            }

        }

        return sb.toString();

    }
