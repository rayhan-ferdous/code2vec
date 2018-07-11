                    lineLength = 0;

                    return '\n';

                } else {

                    lineLength++;

                    int b = buffer[position++];

                    if (position >= bufferLength) position = -1;

                    return b & 0xFF;

                }

            } else {

                throw new java.io.IOException("Error in Base64 code reading stream.");

            }

        }



        /**

         * Calls {@link #read()} repeatedly until the end of stream

         * is reached or <var>len</var> bytes are read.

         * Returns number of bytes read into array or -1 if

         * end of stream is encountered.

         *

         * @param dest array to hold values

         * @param off offset for array

         * @param len max number of bytes to read into array

         * @return bytes read into array or -1 if end of stream is encountered.

         * @since 1.3

         */

        public int read(byte[] dest, int off, int len) throws java.io.IOException {

            int i;

            int b;

            for (i = 0; i < len; i++) {

                b = read();
