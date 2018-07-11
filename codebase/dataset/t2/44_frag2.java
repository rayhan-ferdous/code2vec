    public static String getPathName(File f) {

        try {

            return FileUtil.getPathName(f.getCanonicalPath());

        } catch (IOException e) {

            e.printStackTrace();

            return null;

        }

    }
