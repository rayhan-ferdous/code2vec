        return new File(path, AntScript.LIBNAME_PROGRESS_LOGGER);

    }



    /**

     * Attempt to fix possible race condition when creating directories on

     * WinXP, also Windows2000. If the mkdirs does not work, wait a little and

     * try again.

     * Taken from Ant Mkdir taskdef.

     *

     * @param f the path for which directories are to be created

     * @return <code>true</code> if and only if the directory was created,

     *         along with all necessary parent directories; <code>false</code>

     *         otherwise

     */

    private static boolean doMkDirs(File f) {

        if (!f.mkdirs()) {

            try {

                Thread.sleep(10);

                return f.mkdirs();

            } catch (InterruptedException ex) {

                return f.mkdirs();
