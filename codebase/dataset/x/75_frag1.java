            return filename.substring(pos + 1);

        } else {

            return filename;

        }

    }



    public static String getFolder(final String filename) {

        int pos = filename.lastIndexOf(File.separator);

        if (pos != -1) {

            return getFilename(filename.substring(0, pos));

        } else {

            return "";
