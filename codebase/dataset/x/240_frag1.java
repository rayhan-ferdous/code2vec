    public static String byteArrayToHexString(byte[] b) {

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < b.length; i++) {

            int j = ((int) b[i]) & 0xFF;

            buf.append(digitoHexadecimal.charAt(j / 16));

            buf.append(digitoHexadecimal.charAt(j % 16));

        }

        return buf.toString();

    }
