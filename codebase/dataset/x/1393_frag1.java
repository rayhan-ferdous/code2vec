    public FileHandle getFileHandle(long fileNum) throws FileNotFoundException, ChecksumException, DatabaseException {

        Long fileId = Long.valueOf(fileNum);

        FileHandle fileHandle = null;

        try {

            while (true) {

                fileHandle = fileCache.get(fileId);

                boolean newHandle = false;

                if (fileHandle == null) {

                    if (EnvironmentImpl.getFairLatches()) {

                        fileCacheLatch.acquire();

                        try {

                            fileHandle = fileCache.get(fileId);

                            if (fileHandle == null) {

                                newHandle = true;

                                fileHandle = addFileHandle(fileId);

                            }

                        } finally {

                            fileCacheLatch.release();

                        }

                    } else {

                        synchronized (fileCacheLatch) {

                            fileHandle = fileCache.get(fileId);

                            if (fileHandle == null) {

                                newHandle = true;

                                fileHandle = addFileHandle(fileId);

                            }

                        }

                    }

                }

                if (newHandle) {

                    boolean success = false;

                    try {

                        openFileHandle(fileHandle, FileMode.READ_MODE);

                        success = true;

                    } finally {

                        if (!success) {

                            fileHandle.release();

                            clearFileCache(fileNum);

                        }

                    }

                } else {

                    fileHandle.latch();

                }

                if (fileHandle.getFile() == null) {

                    fileHandle.release();

                } else {

                    break;

                }

            }

        } catch (FileNotFoundException e) {

            throw e;

        } catch (IOException e) {

            throw new EnvironmentFailureException(envImpl, EnvironmentFailureReason.LOG_READ, e);

        }

        return fileHandle;

    }
