        return id;

    }



    /**

     * Convenience method to convert a byte array to a hex string.

     *

     * From http://forum.java.sun.com/thread.jsp?thread=252591&forum=31&message=936765

     *

     * @param data the byte[] to convert

     * @return String the converted byte[]

     */

    public static String bytesToHex(byte[] data) {

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < data.length; i++) {

            buf.append(byteToHex(data[i]));

        }

        return (buf.toString());
