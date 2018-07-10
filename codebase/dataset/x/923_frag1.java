    public static ArrayList getAllFiles(String path) throws IOException {

        File file = new File(path);

        ArrayList ret = new ArrayList();

        String[] listFile = file.list();

        if (listFile != null) {

            for (int i = 0; i < listFile.length; i++) {

                File tempfile = new File(path + LSystem.FS + listFile[i]);

                if (tempfile.isDirectory()) {

                    ArrayList arr = getAllFiles(tempfile.getPath());

                    ret.addAll(arr);

                    arr.clear();

                    arr = null;

                } else {

                    ret.add(tempfile.getAbsolutePath());

                }

            }

        }

        return ret;

    }
