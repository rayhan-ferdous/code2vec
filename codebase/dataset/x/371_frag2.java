        public boolean endOfStream() {

            try {

                return (in.available() == -1);

            } catch (IOException ioe) {

                return true;

            }

        }
