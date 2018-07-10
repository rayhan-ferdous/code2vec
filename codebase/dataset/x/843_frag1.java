        public void run() {

            log.info("ByteStation listening on port " + ssc.socket().getLocalPort());

            while (run) {

                try {

                    SocketChannel s = ssc.accept();

                    SocketSession session = createSession(s);

                    new ByteListener(session, receiver);

                    String address = s.socket().getInetAddress().getHostAddress();

                    log.info("Adding socket connection: " + address);

                } catch (Exception e) {

                    log.warning("Error in accepting new socket connection.");

                    e.printStackTrace();
