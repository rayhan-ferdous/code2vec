    public static String bytesToHexString(byte[] bytes) {

        StringMaker sm = new StringMaker(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {

            String hex = Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1);

            if (hex.length() < 2) {

                sm.append("0");

            }

            sm.append(hex);

        }

        return sm.toString();

    }
