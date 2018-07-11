        try {

            baos = new java.io.ByteArrayOutputStream();

            b64os = new Base64.OutputStream(baos, ENCODE | options);

            if (gzip == GZIP) {

                gzos = new java.util.zip.GZIPOutputStream(b64os);

                oos = new java.io.ObjectOutputStream(gzos);

            } else oos = new java.io.ObjectOutputStream(b64os);

            oos.writeObject(serializableObject);

        } catch (java.io.IOException e) {

            e.printStackTrace();

            return null;

        } finally {

            try {

                oos.close();

            } catch (Exception e) {

            }

            try {

                gzos.close();

            } catch (Exception e) {

            }

            try {

                b64os.close();

            } catch (Exception e) {

            }

            try {

                baos.close();

            } catch (Exception e) {

            }

        }

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

                try {

                    gzos.close();

                } catch (Exception e) {

                }

                try {

                    b64os.close();

                } catch (Exception e) {

                }

                try {

                    baos.close();

                } catch (Exception e) {

                }

            }

            try {

                return new String(baos.toByteArray(), PREFERRED_ENCODING);

            } catch (java.io.UnsupportedEncodingException uue) {

                return new String(baos.toByteArray());

            }

        } else {

            boolean breakLines = dontBreakLines == 0;

            int len43 = len * 4 / 3;

            byte[] outBuff = new byte[(len43) + ((len % 3) > 0 ? 4 : 0) + (breakLines ? (len43 / MAX_LINE_LENGTH) : 0)];

            int d = 0;

            int e = 0;

            int len2 = len - 2;

            int lineLength = 0;

            for (; d < len2; d += 3, e += 4) {

                encode3to4(source, d + off, 3, outBuff, e, options);

                lineLength += 4;

                if (breakLines && lineLength == MAX_LINE_LENGTH) {

                    outBuff[e + 4] = NEW_LINE;

                    e++;

                    lineLength = 0;

                }

            }

            if (d < len) {

                encode3to4(source, d + off, len - d, outBuff, e, options);

                e += 4;

            }

            try {

                return new String(outBuff, 0, e, PREFERRED_ENCODING);

            } catch (java.io.UnsupportedEncodingException uue) {

                return new String(outBuff, 0, e);

            }

        }

    }



    /**

     * Decodes four bytes from array <var>source</var>

     * and writes the resulting bytes (up to three of them)

     * to <var>destination</var>.

     * The source and destination arrays can be manipulated

     * anywhere along their length by specifying 

     * <var>srcOffset</var> and <var>destOffset</var>.

     * This method does not check to make sure your arrays

     * are large enough to accomodate <var>srcOffset</var> + 4 for

     * the <var>source</var> array or <var>destOffset</var> + 3 for

     * the <var>destination</var> array.

     * This method returns the actual number of bytes that 

     * were converted from the Base64 encoding.

	 * <p>This is the lowest level of the decoding methods with

	 * all possible parameters.</p>

     * 

     *

     * @param source the array to convert

     * @param srcOffset the index where conversion begins

     * @param destination the array to hold the conversion

     * @param destOffset the index where output will be put

	 * @param options alphabet type is pulled from this (standard, url-safe, ordered)

     * @return the number of decoded bytes converted

     * @since 1.3

     */

    private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, int options) {

        byte[] DECODABET = getDecodabet(options);

        if (source[srcOffset + 2] == EQUALS_SIGN) {

            int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12);

            destination[destOffset] = (byte) (outBuff >>> 16);

            return 1;

        } else if (source[srcOffset + 3] == EQUALS_SIGN) {

            int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12) | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6);

            destination[destOffset] = (byte) (outBuff >>> 16);

            destination[destOffset + 1] = (byte) (outBuff >>> 8);

            return 2;

        } else {

            try {

                int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12) | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6) | ((DECODABET[source[srcOffset + 3]] & 0xFF));

                destination[destOffset] = (byte) (outBuff >> 16);

                destination[destOffset + 1] = (byte) (outBuff >> 8);

                destination[destOffset + 2] = (byte) (outBuff);

                return 3;

            } catch (Exception e) {

                System.out.println("" + source[srcOffset] + ": " + (DECODABET[source[srcOffset]]));

                System.out.println("" + source[srcOffset + 1] + ": " + (DECODABET[source[srcOffset + 1]]));

                System.out.println("" + source[srcOffset + 2] + ": " + (DECODABET[source[srcOffset + 2]]));

                System.out.println("" + source[srcOffset + 3] + ": " + (DECODABET[source[srcOffset + 3]]));

                return -1;

            }

        }

    }



    /**

     * Very low-level access to decoding ASCII characters in

     * the form of a byte array. Does not support automatically

     * gunzipping or any other "fancy" features.

     *

     * @param source The Base64 encoded data

     * @param off    The offset of where to begin decoding

     * @param len    The length of characters to decode

     * @return decoded data

     * @since 1.3

     */

    public static byte[] decode(byte[] source, int off, int len, int options) {

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
