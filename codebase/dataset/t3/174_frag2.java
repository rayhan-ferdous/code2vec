            public void run() {

                try {

                    while (true) new HTTPSession(ss.accept());

                } catch (IOException ioe) {

                }

            }
