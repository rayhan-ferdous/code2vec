    public static void closeQuietly(Writer writer) {

        if (writer != null) {

            try {

                writer.close();

            } catch (IOException e) {

                logger.warn("problem-closing-quietly", writer, e);

            }

        }

    }
