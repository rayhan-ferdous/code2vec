        public void run() {

            int len;

            byte[] buf = new byte[1024];

            try {

                while ((len = _is.read(buf)) != -1) {

                    _buf.write(buf, 0, len);

                }

            } catch (IOException e) {

                _e = e;

            }

        }
