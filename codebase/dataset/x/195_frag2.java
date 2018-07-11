        String result;



        CygPathCommand(String path) throws IOException {

            command = new String[] { "cygpath", "-u", path };

            run();

        }



        String getResult() throws IOException {

            return result;

        }



        protected String[] getExecString() {

            return command;

        }



        protected void parseExecResult(BufferedReader lines) throws IOException {

            String line = lines.readLine();
