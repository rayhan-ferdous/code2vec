        public void finished() {

            try {

                inputFile.close();

            } catch (IOException ignored) {

            }

            try {

                fileChannel.close();

            } catch (IOException ignored) {

            }

        }
