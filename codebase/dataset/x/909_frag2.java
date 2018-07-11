    public void saveFilelist() {

        try {

            SafeFileWriter out = new SafeFileWriter("files.mlst");

            try {

                MLSTSerialize.serialize(getRoot(), out);

            } finally {

                out.close();

            }

        } catch (IOException e) {

            logger.warn("Error saving files.mlst", e);

        }

    }
