    public void close() {

        try {

            super.close();

        } finally {

            try {

                if (socket != null) socket.close();

            } catch (IOException ioexception) {

                Logger.printException(ioexception);

            }

        }

    }
