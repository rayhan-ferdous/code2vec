    public String getFileStorageDir() {

        String dir = properties.getProperty(PEERSERVER_FILE_STORAGE_DIR);

        if (dir != null) {

            return dir;

        }

        return "../UserFiles";

    }
