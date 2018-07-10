    public static byte[] getBytes(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int c = in.read();

        while (c != -1) {

            out.write(c);

            c = in.read();

        }

        out.close();

        return out.toByteArray();

    }
