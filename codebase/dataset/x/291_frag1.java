            public void run() {

                try {

                    handler.failure(channel, data, ee);

                } catch (Exception e) {

                    log.log(Level.WARNING, channel + "Exception", e);

                }

            }
