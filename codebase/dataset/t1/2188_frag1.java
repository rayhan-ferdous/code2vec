        public int read(byte[] dest, int off, int len) throws java.io.IOException {

            int i;

            int b;

            for (i = 0; i < len; i++) {

                b = read();

                if (b >= 0) dest[off + i] = (byte) b; else if (i == 0) return -1; else break;

            }

            return i;

        }
