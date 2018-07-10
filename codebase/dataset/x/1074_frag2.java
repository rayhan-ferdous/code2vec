    private void recursiveRemoveDir(File directory) {

        String[] filelist = directory.list();

        File tmpFile = null;

        for (int i = 0; i < filelist.length; i++) {

            tmpFile = new File(directory.getAbsolutePath(), filelist[i]);

            if (tmpFile.isDirectory()) {

                recursiveRemoveDir(tmpFile);

            } else if (tmpFile.isFile()) {

                tmpFile.delete();

            }

        }

        directory.delete();

    }
