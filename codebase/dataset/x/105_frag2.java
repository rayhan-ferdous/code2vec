    public static void write(final String text, final OutputStream output) throws IOException {

        DocumentWriter writer = null;

        try {

            writer = new TextWriter(output);

            Content content = writer.beginDefaultContent();

            content.addTextBlock(text);

        } finally {

            FS.close(writer);

        }

    }
