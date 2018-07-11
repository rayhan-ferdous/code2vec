            if (cacheFile.exists()) cacheFile.delete();

            tmpFile.renameTo(cacheFile);

            tmpFile = null;

        } finally {

            lock = false;

        }

    }



    /**

   * Writes out the given flags to be set to value value on the message with

   * the given uid.

   */

    public void setFlags(long uid, Flags f, boolean value) throws IOException {

        BufferedWriter out = null;

        try {

            out = openCacheFile();

            out.write(Long.toString(uid));
