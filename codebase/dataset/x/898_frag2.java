            public void run() {

                try {

                    writeToStreamInChunks();

                } catch (IOException e) {

                    ioe[0] = e;

                }

            }
