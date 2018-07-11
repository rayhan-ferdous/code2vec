    private static final byte[] getAlphabet(int options) {

        if ((options & URL_SAFE) == URL_SAFE) return _URL_SAFE_ALPHABET; else if ((options & ORDERED) == ORDERED) return _ORDERED_ALPHABET; else return _STANDARD_ALPHABET;

    }



    /**

	 * Returns one of the _SOMETHING_DECODABET byte arrays depending on

	 * the options specified.

	 * It's possible, though silly, to specify ORDERED and URL_SAFE

	 * in which case one of them will be picked, though there is

	 * no guarantee as to which one will be picked.

	 */

    private static final byte[] getDecodabet(int options) {
