    public static final void deleteFolderRecursively(File folder) {

        if (folder.isFile()) {

            folder.delete();

            return;

        }

        File[] files = folder.listFiles();

        if (files.length > 0) {

            for (File f : files) {

                if (f.isDirectory()) deleteFolderRecursively(f); else f.delete();

            }

        }

        folder.delete();

    }
