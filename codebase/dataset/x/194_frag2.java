    public static NSArray<File> arrayByAddingFilesInDirectory(File directory, boolean recursive) {

        ERXFile erxDirectory = new ERXFile(directory.getAbsolutePath());

        NSMutableArray<File> files = new NSMutableArray<File>();

        if (!erxDirectory.exists()) {

            return files;

        }

        File[] fileList = erxDirectory.listFiles();

        if (fileList == null) {

            return files;

        }

        for (int i = 0; i < fileList.length; i++) {

            File f = fileList[i];

            if (f.isDirectory() && recursive) {

                files.addObjectsFromArray(ERXFileUtilities.arrayByAddingFilesInDirectory(f, true));

            } else {

                files.addObject(f);

            }

        }

        return files;

    }
