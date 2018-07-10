    public synchronized String uploadFile(String localFileName, String remoteFileName, WriteMode writeMode) throws FTPException, IOException {

        checkTransferSettings();

        boolean append = false;

        if (writeMode.equals(WriteMode.RESUME)) {

            ftpClient.resume();

        } else if (writeMode.equals(WriteMode.APPEND)) {

            append = true;

        }

        return ftpClient.put(localFileName, remoteFileName, append);

    }
