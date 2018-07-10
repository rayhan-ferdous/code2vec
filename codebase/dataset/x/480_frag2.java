        if (datasetFile == null || !datasetFile.exists()) {

            datasetFile = copyFile(Activator.getTempDirectory(), datasetPath);

        }

        return datasetFile;

    }



    private File copyFile(File dir, String path) throws IOException {

        URL entry = bContext.getBundle().getEntry(path);

        path = path.replace('/', File.separatorChar);
