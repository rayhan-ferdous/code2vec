    public static URL getURL(String path) throws ResourceException {

        URL url = Object.class.getResource(path);

        if (url == null) {

            File file = new File(path);

            if (file.exists()) {

                try {

                    url = file.toURI().toURL();

                } catch (MalformedURLException e) {

                    throw new ResourceException(e);

                }

            } else {

                try {

                    url = new URL(path);

                } catch (MalformedURLException e) {

                    throw new ResourceException(e);

                }

            }

        }

        return url;

    }
