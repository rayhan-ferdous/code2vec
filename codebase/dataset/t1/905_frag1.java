    protected boolean sameSenoneSequence(int[] ssid1, int[] ssid2) {

        if (ssid1.length == ssid2.length) {

            for (int i = 0; i < ssid1.length; i++) {

                if (ssid1[i] != ssid2[i]) {

                    return false;

                }

            }

            return true;

        } else {

            return false;

        }

    }



    /**

     * Gets the senone sequence representing the given senones

     * 

     * @param stateid

     *                is the array of senone state ids

     * 

     * @return the senone sequence associated with the states

     */

    protected SenoneSequence getSenoneSequence(int[] stateid) {

        Senone[] senones = new Senone[stateid.length];

        for (int i = 0; i < stateid.length; i++) {
