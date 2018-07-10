        try {

            return new String(baos.toByteArray(), PREFERRED_ENCODING);

        } catch (java.io.UnsupportedEncodingException uue) {

            return new String(baos.toByteArray());

        }

    }



    /**

     * Encodes a byte array into Base64 notation.

     * Does not GZip-compress data.

     *

     * @param source The data to convert

     * @since 1.4

     */

    public static String encodeBytes(byte[] source) {

        return encodeBytes(source, 0, source.length, NO_OPTIONS);

    }



    /**

     * Encodes a byte array into Base64 notation.

     * <p>

     * Valid options:<pre>

     *   GZIP: gzip-compresses object before encoding it.

     *   DONT_BREAK_LINES: don't break lines at 76 characters

     *     <i>Note: Technically, this makes your encoding non-compliant.</i>

     * </pre>

     * <p>

     * Example: <code>encodeBytes( myData, Base64.GZIP )</code> or

     * <p>

     * Example: <code>encodeBytes( myData, Base64.GZIP | Base64.DONT_BREAK_LINES )</code>

     *

     *

     * @param source The data to convert

     * @param options Specified options

     * @see Base64#GZIP

     * @see Base64#DONT_BREAK_LINES

     * @since 2.0

     */

    public static String encodeBytes(byte[] source, int options) {

        return encodeBytes(source, 0, source.length, options);

    }



    /**

     * Encodes a byte array into Base64 notation.

     * Does not GZip-compress data.

     *

     * @param source The data to convert

     * @param off Offset in array where conversion should begin

     * @param len Length of data to convert

     * @since 1.4

     */

    public static String encodeBytes(byte[] source, int off, int len) {

        return encodeBytes(source, off, len, NO_OPTIONS);

    }



    /**

     * Encodes a byte array into Base64 notation.

     * <p>

     * Valid options:<pre>

     *   GZIP: gzip-compresses object before encoding it.

     *   DONT_BREAK_LINES: don't break lines at 76 characters

     *     <i>Note: Technically, this makes your encoding non-compliant.</i>

     * </pre>

     * <p>

     * Example: <code>encodeBytes( myData, Base64.GZIP )</code> or

     * <p>

     * Example: <code>encodeBytes( myData, Base64.GZIP | Base64.DONT_BREAK_LINES )</code>

     *

     *

     * @param source The data to convert

     * @param off Offset in array where conversion should begin

     * @param len Length of data to convert

     * @param options Specified options

	 * @param options alphabet type is pulled from this (standard, url-safe, ordered)

     * @see Base64#GZIP

     * @see Base64#DONT_BREAK_LINES

     * @since 2.0

     */

    public static String encodeBytes(byte[] source, int off, int len, int options) {

        int dontBreakLines = (options & DONT_BREAK_LINES);

        int gzip = (options & GZIP);

        if (gzip == GZIP) {

            java.io.ByteArrayOutputStream baos = null;

            java.util.zip.GZIPOutputStream gzos = null;

            Base64.OutputStream b64os = null;

            try {

                baos = new java.io.ByteArrayOutputStream();

                b64os = new Base64.OutputStream(baos, ENCODE | options);

                gzos = new java.util.zip.GZIPOutputStream(b64os);

                gzos.write(source, off, len);

                gzos.close();

            } catch (java.io.IOException e) {

                e.printStackTrace();

                return null;

            } finally {
