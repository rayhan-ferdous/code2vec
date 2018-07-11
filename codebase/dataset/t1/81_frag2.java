    public static void copy(InputStream in, OutputStream out) throws IOException {

        try {

            byte[] buf = new byte[1024];

            int len;

            while ((len = in.read(buf)) > 0) {

                out.write(buf, 0, len);

            }

        } finally {

            if (out != null) out.close();

        }

    }
