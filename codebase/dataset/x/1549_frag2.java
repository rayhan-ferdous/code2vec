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
