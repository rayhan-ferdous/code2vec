    private void storeFileCreationTimeIfNecessary(File activeLogFileDirectory, File newActiveLogFile) throws IOException {

        long creationTime = System.currentTimeMillis();

        try {

            File creationTimeFile = getCreationTimeFile(activeLogFileDirectory, newActiveLogFile);

            boolean recordCreationTime = !creationTimeFile.exists();

            if (recordCreationTime) {

                FileWriter creationTimeFileOut = new FileWriter(creationTimeFile);

                creationTimeFileOut.write(String.valueOf(creationTime));

                creationTimeFileOut.close();

            }

        } catch (IOException e) {

            throw new IOException("Failed to write file creation time: " + e);

        }

    }
