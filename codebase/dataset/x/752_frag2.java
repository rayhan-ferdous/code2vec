    private static final byte[] getDecodabet(int options) {

        if ((options & URL_SAFE) == URL_SAFE) return _URL_SAFE_DECODABET; else if ((options & ORDERED) == ORDERED) return _ORDERED_DECODABET; else return _STANDARD_DECODABET;

    }



    /** Defeats instantiation. */

    private Base64() {
