    public static void safeClose(Writer writer) {

        if (writer == null) return;

        try {

            writer.close();

        } catch (IOException ex) {

        }

    }
