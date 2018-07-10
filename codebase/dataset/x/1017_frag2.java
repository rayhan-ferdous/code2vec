        public void close() {

            try {

                input.close();

            } catch (Throwable ignore) {

            }

            try {

                method.releaseConnection();

            } catch (Throwable ignore) {

            }

        }
