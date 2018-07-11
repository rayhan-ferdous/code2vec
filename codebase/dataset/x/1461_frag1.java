    public static void transferData(File source, File destination) throws IOException {

        destination.getParentFile().mkdirs();

        InputStream is = null;

        OutputStream os = null;

        try {

            is = new FileInputStream(source);

            os = new FileOutputStream(destination);

            transferData(is, os);

        } finally {

            if (os != null) {

                try {

                    os.close();

                } catch (IOException e) {

                }

            }

            if (is != null) {

                try {

                    is.close();

                } catch (IOException e) {

                }

            }

        }

    }
