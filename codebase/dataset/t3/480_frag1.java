    public static void copy(File src, File dst) {

        InputStream in = null;

        OutputStream out = null;

        try {

            in = new FileInputStream(src);

            out = new FileOutputStream(dst);

            byte[] buf = new byte[1024];

            int len;

            while ((len = in.read(buf)) > 0) {

                out.write(buf, 0, len);

            }

        } catch (IOException e) {

            throw new RuntimeException(e);

        } finally {

            try {

                in.close();

            } catch (Exception e) {

            }

            try {

                out.close();

            } catch (Exception e) {

            }

        }

    }
