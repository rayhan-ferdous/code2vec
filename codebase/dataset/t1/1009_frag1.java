    private final void writeITextFile(final Document doc, final String filepath) throws Exception {

        final FileOutputStream stream = new FileOutputStream(filepath);

        final byte[] data = this.writeITextByteArray(doc);

        try {

            if (stream != null) {

                stream.write(data);

            }

        } finally {

            if (stream != null) {

                stream.close();

            }

        }

    }
