        public void write(byte[] theBytes, int off, int len) throws java.io.IOException {

            if (suspendEncoding) {

                super.out.write(theBytes, off, len);

                return;

            }

            for (int i = 0; i < len; i++) {

                write(theBytes[off + i]);

            }

        }



        /**

		 * Method added by PHIL. [Thanks, PHIL. -Rob] This pads the buffer

		 * without closing the stream.

		 */

        public void flushBase64() throws java.io.IOException {

            if (position > 0) {

                if (encode) {

                    out.write(encode3to4(b4, buffer, position, options));

                    position = 0;

                } else {
