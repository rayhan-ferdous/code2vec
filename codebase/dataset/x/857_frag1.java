            if (is != null) try {

                is.close();

            } catch (IOException e) {

            }

            if (os != null) try {

                os.close();

            } catch (IOException e) {

            }

        }



        public int read(byte[] a, int off, int length) throws ErrnoException {

            if (is == null) return super.read(a, off, length);
