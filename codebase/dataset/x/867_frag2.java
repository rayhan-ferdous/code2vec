    public static String readResourceAsString(String resource, ClassLoader loader) throws IOException {

        InputStream stream = loader.getResourceAsStream(resource);

        try {

            return read(stream);

        } finally {

            stream.close();

        }

    }
