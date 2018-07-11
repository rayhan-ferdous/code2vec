    public static String getUniqueFileName(IPath containerFullPath, String fileName, String extension) {

        if (containerFullPath == null) {

            containerFullPath = new Path("");

        }

        if (fileName == null || fileName.trim().length() == 0) {

            fileName = "default";

        }

        IPath filePath = containerFullPath.append(fileName);

        if (extension != null && !extension.equals(filePath.getFileExtension())) {

            filePath = filePath.addFileExtension(extension);

        }

        extension = filePath.getFileExtension();

        fileName = filePath.removeFileExtension().lastSegment();

        int i = 1;

        while (ResourcesPlugin.getWorkspace().getRoot().exists(filePath)) {

            i++;

            filePath = containerFullPath.append(fileName + i);

            if (extension != null) {

                filePath = filePath.addFileExtension(extension);

            }

        }

        return filePath.lastSegment();

    }
