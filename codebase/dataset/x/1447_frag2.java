        return true;

    }



    /**

     *  Compute the session ID using MD5. TODO fn belongs elsewhere?

     *      @param cookie the session cookie

     *      @param hostKeyN the host key

     *      @param sessionKeyN the host public key

     *      @returns the session ID

     */

    private byte[] computeSessionId(byte[] cookie, BigInteger hostKeyN, BigInteger sessionKeyN) {

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(hostKeyN.abs().toByteArray());

            md.update(sessionKeyN.abs().toByteArray());

            md.update(cookie, 0, 8);
