    protected InputStream getFileStream(String path) {

        try {

            return new FileInputStream(path);

        } catch (Exception e) {

            return null;

        }

    }
