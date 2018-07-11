        public Object next() {

            try {

                return ((ImageReaderSpi) it.next()).createReaderInstance(readerExtension);

            } catch (IOException e) {

                return null;

            }

        }
