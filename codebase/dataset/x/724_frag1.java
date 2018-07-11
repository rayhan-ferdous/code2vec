    @Override

    public void delete(String remoteFilePath) {

        try {

            client.delete(remoteFilePath);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
