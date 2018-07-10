    public static void forceDelete(File file) throws IOException {

        if (file.isDirectory()) {

            deleteDirectory(file);

        } else {

            if (!file.exists()) {

                throw new FileNotFoundException("File does not exist: " + file);

            }

            if (!file.delete()) {

                String message = "Unable to delete file: " + file;

                throw new IOException(message);

            }

        }

    }
