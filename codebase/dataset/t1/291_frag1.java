            if (!Thread.currentThread().getName().startsWith(MyThreadFactory.PREFIX)) {

                errors.add("[onDisconnect] should be executed by a worker thread not by " + Thread.currentThread().getName());

            }

            if (!isInitialized) {

                errors.add("[onDisconnect]  should be initialized");

            }

            if (isDestroyed) {

                errors.add("onDisconnect]  shouldn't be isDestroyed");

            }

            State state = (State) connection.getAttachment();

            if (state == null) {

                errors.add("[onDisconnect] connection doesn't contains state attachment (state should be connected)");

            } else {

                if (state != State.CONNECTED) {

                    errors.add("[onDisconnect] connection  should be in state connected. not in " + state);

                } else {

                    connection.setAttachment(State.DISCONNECTED);

                }

            }

            return true;

        }



        public boolean onConnectionTimeout(INonBlockingConnection connection) throws IOException {
