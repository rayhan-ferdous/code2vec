    protected final boolean isDirectory(FileSystem dfs, Path path) throws ArUnvalidIndexException {

        try {

            FileStatus status = dfs.getFileStatus(path);

            return status.isDirectory();

        } catch (FileNotFoundException e) {

            return false;

        } catch (IOException e) {

            throw new ArUnvalidIndexException("Issue while accessing the Path", e);

        }

    }
