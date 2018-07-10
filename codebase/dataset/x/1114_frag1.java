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
