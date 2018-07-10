    private static String toHex(byte[] digest) {

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < digest.length; i++) {

            buf.append(Integer.toHexString(0x0100 + (digest[i] & 0x00ff)).substring(1));

        }

        return buf.toString();

    }
