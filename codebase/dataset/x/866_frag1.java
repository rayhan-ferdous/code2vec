    public static void close(OutputStream stream) {

        if (stream != null) {

            try {

                stream.flush();

                stream.close();

            } catch (Exception e) {

            }

        }

    }
