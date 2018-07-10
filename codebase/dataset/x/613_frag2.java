    private byte[] getClassBytes(InputStream is) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        BufferedInputStream bis = new BufferedInputStream(is);

        boolean eof = false;

        while (!eof) {

            try {

                int i = bis.read();

                if (i == -1) eof = true; else baos.write(i);

            } catch (IOException e) {

                return null;

            }

        }

        return baos.toByteArray();

    }
