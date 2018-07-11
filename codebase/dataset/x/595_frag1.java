    private static synchronized URL[] getDefaultCodebaseURLs() throws MalformedURLException {

        if (codebaseURLs == null) {

            if (codebaseProperty != null) {

                codebaseURLs = pathToURLs(codebaseProperty);

            } else {

                codebaseURLs = new URL[0];

            }

        }

        return codebaseURLs;

    }
