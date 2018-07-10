    private static String toString(InputStream inputStream) throws IOException {

        String string;

        StringBuilder outputBuilder = new StringBuilder();

        if (inputStream != null) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while (null != (string = reader.readLine())) {

                outputBuilder.append(string).append('\n');

            }

        }

        return outputBuilder.toString();

    }
