    public static boolean fileExists(String tOutput) {

        try {

            return fs.exists(new Path(tOutput));

        } catch (IOException e) {

            return false;

        }

    }
