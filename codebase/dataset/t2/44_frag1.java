    public static String getPath(File f) {

        try {

            return FileUtil.getPath(f.getCanonicalPath());

        } catch (IOException e) {

            e.printStackTrace();

            return null;

        }

    }
