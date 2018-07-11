    public void delete(String fileName) {

        panda.server.Panda.getBufferManager().clear(fileName);

        if (openFiles.containsKey(fileName)) {

            try {

                openFiles.get(fileName).close();

                openFiles.remove(fileName);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        dbRootDirectroy.deleteFile(fileName);

    }
