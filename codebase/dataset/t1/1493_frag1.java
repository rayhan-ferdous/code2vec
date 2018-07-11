    private static final byte[] getDecodabet(int options) {

        if ((options & URL_SAFE) == URL_SAFE) return _URL_SAFE_DECODABET; else if ((options & ORDERED) == ORDERED) return _ORDERED_DECODABET; else return _STANDARD_DECODABET;

    }



    /** Defeats instantiation. */

    private Base64() {

    }



    /**

     * Encodes or decodes two files from the command line;

     * <strong>feel free to delete this method (in fact you probably should)

     * if you're embedding this code into a larger program.</strong>

     */

    public static final void main(String[] args) {

        if (args.length < 3) {

            usage("Not enough arguments.");

        } else {

            String flag = args[0];

            String infile = args[1];

            String outfile = args[2];

            if (flag.equals("-e")) {

                Base64.encodeFileToFile(infile, outfile);

            } else if (flag.equals("-d")) {

                Base64.decodeFileToFile(infile, outfile);

            } else {

                usage("Unknown flag: " + flag);

            }

        }

    }



    /**

     * Prints command line usage.

     *

     * @param msg A message to include with usage info.

     */

    private static final void usage(String msg) {

        System.err.println(msg);

        System.err.println("Usage: java Base64 -e|-d inputfile outputfile");

    }



    /**

     * Encodes up to the first three bytes of array <var>threeBytes</var>

     * and returns a four-byte array in Base64 notation.

     * The actual number of significant bytes in your array is

     * given by <var>numSigBytes</var>.

     * The array <var>threeBytes</var> needs only be as big as

     * <var>numSigBytes</var>.

     * Code can reuse a byte array by passing a four-byte array as <var>b4</var>.

     *

     * @param b4 A reusable byte array to reduce array instantiation

     * @param threeBytes the array to convert

     * @param numSigBytes the number of significant bytes in your array

     * @return four byte array in Base64 notation.

     * @since 1.5.1

     */

    private static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes, int options) {

        encode3to4(threeBytes, 0, numSigBytes, b4, 0, options);

        return b4;

    }



    /**

     * <p>Encodes up to three bytes of the array <var>source</var>

     * and writes the resulting four Base64 bytes to <var>destination</var>.

     * The source and destination arrays can be manipulated

     * anywhere along their length by specifying 

     * <var>srcOffset</var> and <var>destOffset</var>.

     * This method does not check to make sure your arrays

     * are large enough to accomodate <var>srcOffset</var> + 3 for

     * the <var>source</var> array or <var>destOffset</var> + 4 for

     * the <var>destination</var> array.

     * The actual number of significant bytes in your array is

     * given by <var>numSigBytes</var>.</p>

	 * <p>This is the lowest level of the encoding methods with

	 * all possible parameters.</p>

     *

     * @param source the array to convert

     * @param srcOffset the index where conversion begins

     * @param numSigBytes the number of significant bytes in your array

     * @param destination the array to hold the conversion

     * @param destOffset the index where output will be put

     * @return the <var>destination</var> array

     * @since 1.3

     */

    private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options) {

        byte[] ALPHABET = getAlphabet(options);

        int inBuff = (numSigBytes > 0 ? ((source[srcOffset] << 24) >>> 8) : 0) | (numSigBytes > 1 ? ((source[srcOffset + 1] << 24) >>> 16) : 0) | (numSigBytes > 2 ? ((source[srcOffset + 2] << 24) >>> 24) : 0);

        switch(numSigBytes) {

            case 3:

                destination[destOffset] = ALPHABET[(inBuff >>> 18)];

                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];

                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];

                destination[destOffset + 3] = ALPHABET[(inBuff) & 0x3f];

                return destination;

            case 2:

                destination[destOffset] = ALPHABET[(inBuff >>> 18)];

                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];

                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];

                destination[destOffset + 3] = EQUALS_SIGN;

                return destination;

            case 1:

                destination[destOffset] = ALPHABET[(inBuff >>> 18)];

                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];

                destination[destOffset + 2] = EQUALS_SIGN;

                destination[destOffset + 3] = EQUALS_SIGN;

                return destination;

            default:

                return destination;

        }

    }



    /**

     * Serializes an object and returns the Base64-encoded

     * version of that serialized object. If the object

     * cannot be serialized or there is another error,

     * the method will return <tt>null</tt>.

     * The object is not GZip-compressed before being encoded.

     *

     * @param serializableObject The object to encode

     * @return The Base64-encoded object

     * @since 1.4

     */

    public static String encodeObject(java.io.Serializable serializableObject) {

        return encodeObject(serializableObject, NO_OPTIONS);

    }



    /**

     * Serializes an object and returns the Base64-encoded

     * version of that serialized object. If the object

     * cannot be serialized or there is another error,

     * the method will return <tt>null</tt>.

     * <p>

     * Valid options:<pre>

     *   GZIP: gzip-compresses object before encoding it.

     *   DONT_BREAK_LINES: don't break lines at 76 characters

     *     <i>Note: Technically, this makes your encoding non-compliant.</i>

     * </pre>

     * <p>

     * Example: <code>encodeObject( myObj, Base64.GZIP )</code> or

     * <p>

     * Example: <code>encodeObject( myObj, Base64.GZIP | Base64.DONT_BREAK_LINES )</code>

     *

     * @param serializableObject The object to encode

     * @param options Specified options

     * @return The Base64-encoded object

     * @see Base64#GZIP

     * @see Base64#DONT_BREAK_LINES

     * @since 2.0

     */

    public static String encodeObject(java.io.Serializable serializableObject, int options) {
