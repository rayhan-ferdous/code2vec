    private static void copyFile(File src, File dst) throws FileNotFoundException, IOException {

        InputStream in = null;

        try {

            in = new FileInputStream(src);

            copy(in, dst);

        } finally {

            if (in != null) {

                try {

                    in.close();

                } catch (IOException e) {

                    log.error(Messages.getString("FileHelper.UNABLE_CLOSE_INPUT_STREAM"), e);

                }

            }

        }

    }
