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

                System.err.println("Bad Base64 input character at " + i + ": " + source[i] + "(decimal)");

                return null;

            }

        }

        byte[] out = new byte[outBuffPosn];

        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);

        return out;

    }



    /**

     * Decodes data from Base64 notation, automatically

     * detecting gzip-compressed data and decompressing it.

     *

     * @param s the string to decode

     * @return the decoded data

     * @since 1.4

     */

    public static byte[] decode(String s) {

        return decode(s, NO_OPTIONS);

    }



    /**

     * Decodes data from Base64 notation, automatically

     * detecting gzip-compressed data and decompressing it.

     *

     * @param s the string to decode

	 * @param options encode options such as URL_SAFE

     * @return the decoded data

     * @since 1.4

     */

    public static byte[] decode(String s, int options) {

        byte[] bytes;

        try {

            bytes = s.getBytes(PREFERRED_ENCODING);

        } catch (java.io.UnsupportedEncodingException uee) {

            bytes = s.getBytes();

        }

        bytes = decode(bytes, 0, bytes.length, options);

        if (bytes != null && bytes.length >= 4) {

            int head = ((int) bytes[0] & 0xff) | ((bytes[1] << 8) & 0xff00);

            if (java.util.zip.GZIPInputStream.GZIP_MAGIC == head) {

                java.io.ByteArrayInputStream bais = null;

                java.util.zip.GZIPInputStream gzis = null;

                java.io.ByteArrayOutputStream baos = null;

                byte[] buffer = new byte[2048];

                int length = 0;

                try {

                    baos = new java.io.ByteArrayOutputStream();

                    bais = new java.io.ByteArrayInputStream(bytes);

                    gzis = new java.util.zip.GZIPInputStream(bais);

                    while ((length = gzis.read(buffer)) >= 0) {

                        baos.write(buffer, 0, length);

                    }

                    bytes = baos.toByteArray();

                } catch (java.io.IOException e) {

                } finally {

                    try {

                        baos.close();

                    } catch (Exception e) {

                    }

                    try {

                        gzis.close();

                    } catch (Exception e) {

                    }

                    try {

                        bais.close();

                    } catch (Exception e) {

                    }

                }

            }

        }

        return bytes;

    }



    /**

     * Attempts to decode Base64 data and deserialize a Java

     * Object within. Returns <tt>null</tt> if there was an error.

     *

     * @param encodedObject The Base64 data to decode

     * @return The decoded and deserialized object

     * @since 1.5

     */

    public static Object decodeToObject(String encodedObject) {

        byte[] objBytes = decode(encodedObject);

        java.io.ByteArrayInputStream bais = null;

        java.io.ObjectInputStream ois = null;

        Object obj = null;

        try {

            bais = new java.io.ByteArrayInputStream(objBytes);

            ois = new java.io.ObjectInputStream(bais);

            obj = ois.readObject();

        } catch (java.io.IOException e) {

            e.printStackTrace();

            obj = null;

        } catch (java.lang.ClassNotFoundException e) {

            e.printStackTrace();

            obj = null;

        } finally {

            try {

                bais.close();

            } catch (Exception e) {

            }

            try {

                ois.close();

            } catch (Exception e) {

            }

        }

        return obj;

    }



    /**

     * Convenience method for encoding data to a file.

     *

     * @param dataToEncode byte array of data to encode in base64 form

     * @param filename Filename for saving encoded data

     * @return <tt>true</tt> if successful, <tt>false</tt> otherwise

     *

     * @since 2.1

     */

    public static boolean encodeToFile(byte[] dataToEncode, String filename) {

        boolean success = false;

        Base64.OutputStream bos = null;

        try {

            bos = new Base64.OutputStream(new java.io.FileOutputStream(filename), Base64.ENCODE);

            bos.write(dataToEncode);

            success = true;

        } catch (java.io.IOException e) {

            success = false;

        } finally {

            try {

                bos.close();

            } catch (Exception e) {

            }

        }

        return success;

    }



    /**

     * Convenience method for decoding data to a file.

     *

     * @param dataToDecode Base64-encoded data as a string

     * @param filename Filename for saving decoded data

     * @return <tt>true</tt> if successful, <tt>false</tt> otherwise

     *

     * @since 2.1

     */

    public static boolean decodeToFile(String dataToDecode, String filename) {

        boolean success = false;

        Base64.OutputStream bos = null;

        try {

            bos = new Base64.OutputStream(new java.io.FileOutputStream(filename), Base64.DECODE);

            bos.write(dataToDecode.getBytes(PREFERRED_ENCODING));

            success = true;

        } catch (java.io.IOException e) {

            success = false;

        } finally {

            try {

                bos.close();

            } catch (Exception e) {

            }

        }

        return success;

    }



    /**

     * Convenience method for reading a base64-encoded

     * file and decoding it.

     *

     * @param filename Filename for reading encoded data

     * @return decoded byte array or null if unsuccessful

     *

     * @since 2.1

     */

    public static byte[] decodeFromFile(String filename) {

        byte[] decodedData = null;

        Base64.InputStream bis = null;

        try {

            java.io.File file = new java.io.File(filename);

            byte[] buffer = null;

            int length = 0;

            int numBytes = 0;

            if (file.length() > Integer.MAX_VALUE) {

                System.err.println("File is too big for this convenience method (" + file.length() + " bytes).");

                return null;

            }

            buffer = new byte[(int) file.length()];

            bis = new Base64.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(file)), Base64.DECODE);

            while ((numBytes = bis.read(buffer, length, 4096)) >= 0) length += numBytes;

            decodedData = new byte[length];

            System.arraycopy(buffer, 0, decodedData, 0, length);

        } catch (java.io.IOException e) {

            System.err.println("Error decoding from file " + filename);

        } finally {

            try {

                bis.close();

            } catch (Exception e) {

            }

        }

        return decodedData;

    }



    /**

     * Convenience method for reading a binary file

     * and base64-encoding it.

     *

     * @param filename Filename for reading binary data

     * @return base64-encoded string or null if unsuccessful

     *

     * @since 2.1

     */

    public static String encodeFromFile(String filename) {

        String encodedData = null;

        Base64.InputStream bis = null;

        try {

            java.io.File file = new java.io.File(filename);

            byte[] buffer = new byte[Math.max((int) (file.length() * 1.4), 40)];

            int length = 0;

            int numBytes = 0;

            bis = new Base64.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(file)), Base64.ENCODE);

            while ((numBytes = bis.read(buffer, length, 4096)) >= 0) length += numBytes;

            encodedData = new String(buffer, 0, length, Base64.PREFERRED_ENCODING);

        } catch (java.io.IOException e) {

            System.err.println("Error encoding from file " + filename);

        } finally {

            try {

                bis.close();

            } catch (Exception e) {

            }

        }

        return encodedData;

    }



    /**

     * Reads <tt>infile</tt> and encodes it to <tt>outfile</tt>.

     *

     * @param infile Input file

     * @param outfile Output file

     * @return true if the operation is successful

     * @since 2.2

     */

    public static boolean encodeFileToFile(String infile, String outfile) {

        boolean success = false;

        java.io.InputStream in = null;

        java.io.OutputStream out = null;

        try {

            in = new Base64.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(infile)), Base64.ENCODE);

            out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outfile));

            byte[] buffer = new byte[65536];

            int read = -1;

            while ((read = in.read(buffer)) >= 0) {

                out.write(buffer, 0, read);

            }

            success = true;

        } catch (java.io.IOException exc) {

            exc.printStackTrace();

        } finally {

            try {

                in.close();

            } catch (Exception exc) {

            }

            try {

                out.close();

            } catch (Exception exc) {

            }

        }

        return success;

    }



    /**

     * Reads <tt>infile</tt> and decodes it to <tt>outfile</tt>.

     *

     * @param infile Input file

     * @param outfile Output file

     * @return true if the operation is successful

     * @since 2.2

     */

    public static boolean decodeFileToFile(String infile, String outfile) {

        boolean success = false;

        java.io.InputStream in = null;

        java.io.OutputStream out = null;

        try {

            in = new Base64.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(infile)), Base64.DECODE);

            out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outfile));

            byte[] buffer = new byte[65536];

            int read = -1;

            while ((read = in.read(buffer)) >= 0) {

                out.write(buffer, 0, read);

            }

            success = true;

        } catch (java.io.IOException exc) {

            exc.printStackTrace();

        } finally {

            try {

                in.close();

            } catch (Exception exc) {

            }

            try {

                out.close();

            } catch (Exception exc) {

            }

        }

        return success;

    }



    /**

     * A {@link Base64.InputStream} will read data from another

     * <tt>java.io.InputStream</tt>, given in the constructor,

     * and encode/decode to/from Base64 notation on the fly.

     *

     * @see Base64

     * @since 1.3

     */

    public static class InputStream extends java.io.FilterInputStream {



        private boolean encode;



        private int position;



        private byte[] buffer;



        private int bufferLength;



        private int numSigBytes;



        private int lineLength;



        private boolean breakLines;



        private int options;



        private byte[] alphabet;



        private byte[] decodabet;



        /**

         * Constructs a {@link Base64.InputStream} in DECODE mode.

         *

         * @param in the <tt>java.io.InputStream</tt> from which to read data.

         * @since 1.3

         */

        public InputStream(java.io.InputStream in) {

            this(in, DECODE);

        }



        /**

         * Constructs a {@link Base64.InputStream} in

         * either ENCODE or DECODE mode.

         * <p>

         * Valid options:<pre>

         *   ENCODE or DECODE: Encode or Decode as data is read.

         *   DONT_BREAK_LINES: don't break lines at 76 characters

         *     (only meaningful when encoding)

         *     <i>Note: Technically, this makes your encoding non-compliant.</i>

         * </pre>

         * <p>

         * Example: <code>new Base64.InputStream( in, Base64.DECODE )</code>

         *

         *

         * @param in the <tt>java.io.InputStream</tt> from which to read data.

         * @param options Specified options

         * @see Base64#ENCODE

         * @see Base64#DECODE

         * @see Base64#DONT_BREAK_LINES

         * @since 2.0

         */

        public InputStream(java.io.InputStream in, int options) {

            super(in);

            this.breakLines = (options & DONT_BREAK_LINES) != DONT_BREAK_LINES;

            this.encode = (options & ENCODE) == ENCODE;

            this.bufferLength = encode ? 4 : 3;

            this.buffer = new byte[bufferLength];

            this.position = -1;

            this.lineLength = 0;

            this.options = options;

            this.alphabet = getAlphabet(options);

            this.decodabet = getDecodabet(options);
