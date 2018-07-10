    private static BufferedReader getHttpReader(URL url, String defaultEncoding) throws IOException, UnsupportedEncodingException {

        try {

            URLConnection connection = getConnection(url);

            connection.connect();

            String encoding = encoding(connection, defaultEncoding);

            InputStream inputStream = connection.getInputStream();

            return new BufferedReader(new InputStreamReader(inputStream, encoding));

        } catch (MalformedURLException e) {

            throw new IllegalArgumentException(e);

        }

    }
