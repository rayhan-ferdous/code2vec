    public static boolean copy(final String from, final String to, final String what) {

        return copy(new File(from, what), new File(to, what));

    }



    /**

   * copy copies a file or a directory from one directory to another

   * @param from directory from where to copy

   * @param to directory where to copy

   * @param what what to copy (file or directory, recursively)

   * @return true if successful

   *

   * <br><br><b>Example</b>:

   * <li><code>copy(new File(myHomeDir, "dev"), new File(myHomeDir, "rtm"), "contents.xml")</code></li>

   */

    public static boolean copy(final File from, final File to, final String what) {

        return copy(new File(from, what), new File(to, what));

    }



    /**

   * copy copies a file or a directory to another

   * @param from

   * @param to

   * @return true if successful

   *

   * <br><br><b>Example</b>:

   * <li><code>copy("c:\\home\\vlad\\dev\\contents.xml", "c:\\home\\vlad\\rtm\\contents.rss")</code></li>

   */

    public static boolean copy(final String from, final String to) {
