    private URL getUrlFromString(String urlString) throws ApolloAdapterException {

        URL url;

        try {

            url = new URL(urlString);

        } catch (MalformedURLException ex) {

            logger.error(ex.getMessage(), ex);

            throw new ApolloAdapterException(ex);

        }

        return (url);

    }
