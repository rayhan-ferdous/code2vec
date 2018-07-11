    public InputStream post(Map<String, String> cookies, Map parameters) throws IOException {

        setCookies(cookies);

        postCookies();

        setParameters(parameters);

        return doPost();

    }
