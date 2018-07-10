    public static String getFilePath(String file) {

        int i = file.lastIndexOf('/');

        if (i < 0) {

            return file;

        }

        return file.substring(0, i);

    }
