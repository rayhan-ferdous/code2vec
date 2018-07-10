    private static File getCanonicalFile(final File file) {

        try {

            return file.getCanonicalFile();

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }
