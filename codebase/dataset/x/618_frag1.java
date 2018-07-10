        return resultSb.toString();

    }



    /**

	 * 转换字节为16进制字符串

	 * 

	 * @param b

	 *            字节数组

	 * @return 16进制字符串

	 */

    private static String byteToHexString(byte b) {

        int n = b;

        if (n < 0) n = 256 + n;

        int d1 = n / 16;

        int d2 = n % 16;

        return hexDigits[d1] + hexDigits[d2];
