        public OutputStream(java.io.OutputStream out) {

            this(out, ENCODE);

        }



        /**

         * Constructs a {@link Base64.OutputStream} in

         * either ENCODE or DECODE mode.

         * <p>

         * Valid options:<pre>

         *   ENCODE or DECODE: Encode or Decode as data is read.

         *   DONT_BREAK_LINES: don't break lines at 76 characters

         *     (only meaningful when encoding)

         *     <i>Note: Technically, this makes your encoding non-compliant.</i>

         * </pre>

         * <p>

         * Example: <code>new Base64.OutputStream( out, Base64.ENCODE )</code>

         *

         * @param out the <tt>java.io.OutputStream</tt> to which data will be written.

         * @param options Specified options.

         * @see Base64#ENCODE

         * @see Base64#DECODE

         * @see Base64#DONT_BREAK_LINES

         * @since 1.3

         */

        public OutputStream(java.io.OutputStream out, int options) {

            super(out);

            this.breakLines = (options & DONT_BREAK_LINES) != DONT_BREAK_LINES;

            this.encode = (options & ENCODE) == ENCODE;

            this.bufferLength = encode ? 3 : 4;

            this.buffer = new byte[bufferLength];

            this.position = 0;

            this.lineLength = 0;

            this.suspendEncoding = false;

            this.b4 = new byte[4];
