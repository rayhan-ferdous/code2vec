    public void close() throws IOException {

        super.close();

        if (mode.equals("r")) {

            return;

        }

        if (!file.exists()) {

            file.createNewFile();

        }

        InputStream is = new java.io.FileInputStream(super.getFD());

        OutputStream os = file.getOutputStream();

        copy(is, os);

    }
