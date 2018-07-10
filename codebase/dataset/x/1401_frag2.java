            int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12) | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6);

            destination[destOffset] = (byte) (outBuff >>> 16);

            destination[destOffset + 1] = (byte) (outBuff >>> 8);

            return 2;

        } else {

            try {

                int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12) | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6) | ((DECODABET[source[srcOffset + 3]] & 0xFF));

                destination[destOffset] = (byte) (outBuff >> 16);

                destination[destOffset + 1] = (byte) (outBuff >> 8);

                destination[destOffset + 2] = (byte) (outBuff);

                return 3;

            } catch (Exception e) {

                System.out.println("" + source[srcOffset] + ": " + (DECODABET[source[srcOffset]]));

                System.out.println("" + source[srcOffset + 1] + ": " + (DECODABET[source[srcOffset + 1]]));

                System.out.println("" + source[srcOffset + 2] + ": " + (DECODABET[source[srcOffset + 2]]));

                System.out.println("" + source[srcOffset + 3] + ": " + (DECODABET[source[srcOffset + 3]]));

                return -1;

            }

        }

    }



    /**

     * Very low-level access to decoding ASCII characters in

     * the form of a byte array. Does not support automatically

     * gunzipping or any other "fancy" features.

     *

     * @param source The Base64 encoded data

     * @param off    The offset of where to begin decoding

     * @param len    The length of characters to decode

     * @return decoded data

     * @since 1.3

     */

    public static byte[] decode(byte[] source, int off, int len, int options) {

        byte[] DECODABET = getDecodabet(options);

        int len34 = len * 3 / 4;

        byte[] outBuff = new byte[len34];

        int outBuffPosn = 0;

        byte[] b4 = new byte[4];

        int b4Posn = 0;

        int i = 0;

        byte sbiCrop = 0;

        byte sbiDecode = 0;

        for (i = off; i < off + len; i++) {

            sbiCrop = (byte) (source[i] & 0x7f);

            sbiDecode = DECODABET[sbiCrop];

            if (sbiDecode >= WHITE_SPACE_ENC) {

                if (sbiDecode >= EQUALS_SIGN_ENC) {

                    b4[b4Posn++] = sbiCrop;

                    if (b4Posn > 3) {

                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options);

                        b4Posn = 0;

                        if (sbiCrop == EQUALS_SIGN) break;

                    }

                }

            } else {

                System.err.println("Bad Base64 input character at " + i + ": " + source[i] + "(decimal)");

                return null;

            }

        }

        byte[] out = new byte[outBuffPosn];

        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);

        return out;

    }



    /**

     * Decodes data from Base64 notation, automatically

     * detecting gzip-compressed data and decompressing it.

     *

     * @param s the string to decode

     * @return the decoded data

     * @since 1.4

     */

    public static byte[] decode(String s) {

        return decode(s, NO_OPTIONS);

    }



    /**

     * Decodes data from Base64 notation, automatically

     * detecting gzip-compressed data and decompressing it.

     *

     * @param s the string to decode

	 * @param options encode options such as URL_SAFE

     * @return the decoded data

     * @since 1.4

     */

    public static byte[] decode(String s, int options) {

        byte[] bytes;

        try {

            bytes = s.getBytes(PREFERRED_ENCODING);
