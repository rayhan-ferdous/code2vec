    public void shutDown() {

        for (ClientHandler ch : this.clientHandlers) {

            ch.shutDown();

        }

        try {

            this.acceptor.channel().close();

        } catch (IOException e) {

            log.error("could not close connection on server shutdown.", e);

        }

    }
