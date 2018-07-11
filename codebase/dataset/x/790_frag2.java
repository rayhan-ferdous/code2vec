    public BufferedReader getReader() {

        if (reader == null) {

            getInputStream();

            if (input != null) reader = new BufferedReader(new InputStreamReader(input));

        }

        return reader;

    }
