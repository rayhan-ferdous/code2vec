public class Base64 {



    /** No options specified. Value is zero. */

    public static final int NO_OPTIONS = 0;



    /** Specify encoding. */

    public static final int ENCODE = 1;



    /** Specify decoding. */

    public static final int DECODE = 0;



    /** Specify that data should be gzip-compressed. */

    public static final int GZIP = 2;



    /** Don't break lines when encoding (violates strict Base64 specification) */

    public static final int DONT_BREAK_LINES = 8;



    /** 

	 * Encode using Base64-like encoding that is URL- and Filename-safe as described

	 * in Section 4 of RFC3548: 

	 * <a href="http://www.faqs.org/rfcs/rfc3548.html">http://www.faqs.org/rfcs/rfc3548.html</a>.

	 * It is important to note that data encoded this way is <em>not</em> officially valid Base64, 

	 * or at the very least should not be called Base64 without also specifying that is

	 * was encoded using the URL- and Filename-safe dialect.

	 */

    public static final int URL_SAFE = 16;



    /**

	  * Encode using the special "ordered" dialect of Base64 described here:

	  * <a href="http://www.faqs.org/qa/rfcc-1940.html">http://www.faqs.org/qa/rfcc-1940.html</a>.

	  */

    public static final int ORDERED = 32;



    /** Maximum line length (76) of Base64 output. */

    private static final int MAX_LINE_LENGTH = 76;



    /** The equals sign (=) as a byte. */

    private static final byte EQUALS_SIGN = (byte) '=';



    /** The new line character (\n) as a byte. */

    private static final byte NEW_LINE = (byte) '\n';



    /** Preferred encoding. */

    private static final String PREFERRED_ENCODING = "UTF-8";
