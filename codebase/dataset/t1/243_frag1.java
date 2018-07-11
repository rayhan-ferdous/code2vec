    protected void parseLineups() throws DataDirectException {

        try {

            log.write(sdf.format(new java.util.Date()));

            log.write("\tParsing lineups top-level element");

            log.write(Parser.END_OF_LINE);

        } catch (IOException ioex) {

            ioex.printStackTrace();

        }
