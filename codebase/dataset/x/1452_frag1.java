            debugOutputLn(EXCEPTION, "getToken: " + e);

            throw new ParsingErrorException(e.getMessage());

        }

    }



    /**

     * Skip ahead amount bytes in the file

     */

    public void skipLength(int amount) throws ParsingErrorException {

        try {

            skip((long) amount);

            marker += amount;

        } catch (IOException e) {

            debugOutputLn(EXCEPTION, "skipLength: " + e);

            throw new ParsingErrorException(e.getMessage());
