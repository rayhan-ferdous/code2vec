    public static boolean copy(String from, String to, String what) {

        return copy(new File(from, what), new File(to, what));

    }



    /**

	 * copy copies a file or a directory from one directory to another

	 * 

	 * @param from

	 *            directory from where to copy

	 * @param to

	 *            directory where to copy

	 * @param what

	 *            what to copy (file or directory, recursively)

	 * @return true if successful <br>

	 *         <br>

	 *         <b>Example</b>:

	 *         <li><code>copy(new File(myHomeDir, "dev"), new File(myHomeDir, "rtm"), "contents.xml")</code></li>

	 */

    public static boolean copy(File from, File to, String what) {

        return copy(new File(from, what), new File(to, what));

    }



    /**

	 * In a "creative" way checks whether a string or a container is empty. <br>

	 * Accepts a <code>Collection</code>, a <code>Map</code>, an array, a <code>String</code>.

	 * 

	 * @param data

	 *            a Collection or a Map or an array or a string to check

	 * @return true if data is empty <br>

	 *         <br>

	 *         <b>Examples</b>:

	 *         <li><code>isEmpty(StringUtils.EMPTY), isEmpty(null), isEmpty(new HashMap())</code> all return

	 *         <b>true</b>;</li>

	 *         <li><code>isEmpty(" "), isEmpty(new int[] {1})</code> returns <b>false</b>.</li>

	 */

    public static final boolean isEmpty(Object data) {
