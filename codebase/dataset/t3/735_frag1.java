    public void copyFile(final String src, final String dest) throws IOException {

        FileInputStream fis = null;

        FileOutputStream fos = null;

        try {

            fis = new FileInputStream(src);

            fos = new FileOutputStream(dest);

            copy(fis, fos);

        } catch (final FileNotFoundException e) {

            throw e;

        } finally {

            if (fis != null) {

                try {

                    fis.close();

                } catch (final IOException e) {

                }

            }

            if (fos != null) {

                try {

                    fos.close();

                } catch (final IOException e) {

                }

            }

        }

    }
