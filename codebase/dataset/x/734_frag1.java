        MessageDigest digest = MessageDigest.getInstance(hashingAlgorithm);

        result = new String(digest.digest(plainTextPassword.getBytes(charsetName)), charsetName);

        return result;

    }



    /**

     * Checks if the supplied user id is valid.

     *

     * @param userId the user id to be checked

     * @return <code>true</code> if the id is valid or <code>false</code> otherwise

     */

    public static boolean isUserIdValid(final int userId) {

        if (userId > Constants.INVALID_USER_ID) return true; else return false;
