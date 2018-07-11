    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {

        if (srcFile == null) {

            throw new NullPointerException("Source must not be null");

        }

        if (destFile == null) {

            throw new NullPointerException("Destination must not be null");

        }

        if (srcFile.exists() == false) {

            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");

        }

        if (srcFile.isDirectory()) {

            throw new IOException("Source '" + srcFile + "' exists but is a directory");

        }

        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {

            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");

        }

        if (destFile.getParentFile() != null && destFile.getParentFile().exists() == false) {

            if (destFile.getParentFile().mkdirs() == false) {

                throw new IOException("Destination '" + destFile + "' directory cannot be created");

            }

        }

        if (destFile.exists() && destFile.canWrite() == false) {

            throw new IOException("Destination '" + destFile + "' exists but is read-only");

        }

        doCopyFile(srcFile, destFile, preserveFileDate);

    }



    /**

     * Internal copy file method.

     * 

     * @param srcFile

     *            the validated source file, must not be <code>null</code>

     * @param destFile

     *            the validated destination file, must not be <code>null</code>

     * @param preserveFileDate

     *            whether to preserve the file date

     * @throws IOException

     *             if an error occurs

     */

    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {

        if (destFile.exists() && destFile.isDirectory()) {

            throw new IOException("Destination '" + destFile + "' exists but is a directory");
