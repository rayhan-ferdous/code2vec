    public int read(byte[] b, int offset, int length) throws IOException {

        int count = 0;

        int read = read();

        if (read == -1) {

            return -1;

        }

        while ((read != -1) && (count < length)) {

            b[offset] = (byte) read;

            read = read();

            count++;

            offset++;

        }

        return count;

    }
