        public void close() throws java.io.IOException {

            flushBase64();

            super.close();

            buffer = null;

            out = null;

        }
