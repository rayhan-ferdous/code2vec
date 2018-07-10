    public static void deleteFile(File directory) {

        File files[] = directory.listFiles();

        if (files != null) {

            for (int i = 0; i < files.length; i++) {

                if (files[i].isDirectory()) {

                    deleteFile(files[i]);

                } else {

                    files[i].delete();

                }

            }

        }

        directory.delete();

    }
