    protected InputStream openTempFileResponseInputStream() {

        FileInputStream newResponseStream = null;

        try {

            File responseFile = getTempResponseFile();

            newResponseStream = new FileInputStream(responseFile);

        } catch (IOException io) {

            System.out.println("Unable to open response temp input file in SAMNotebookServerProxy");

        }

        return newResponseStream;

    }
