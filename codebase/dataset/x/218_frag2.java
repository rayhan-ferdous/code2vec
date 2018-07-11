    public static void close(OutputStream os) {

        try {

            if (os != null) {

                os.close();

            }

        } catch (IOException e) {

        }

    }
