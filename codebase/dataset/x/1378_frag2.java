    private static String getFileExt(String fileName) {

        String ext = "";

        try {

            int mid = fileName.lastIndexOf(".");

            String fname = fileName.substring(0, mid);

            ext = fileName.substring(mid + 1, fileName.length());

        } catch (Exception e) {

            e.printStackTrace();

        }

        return ext;

    }
