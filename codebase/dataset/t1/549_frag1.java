    private static String byteToHexString(byte b) {

        int n = b;

        if (n < 0) n += 256;

        int d1 = n / 16;

        int d2 = n % 16;

        return (new StringBuilder(String.valueOf(hexDigits[d1]))).append(hexDigits[d2]).toString();

    }
