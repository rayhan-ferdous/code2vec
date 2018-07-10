    public void close() {

        try {

            zip.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
