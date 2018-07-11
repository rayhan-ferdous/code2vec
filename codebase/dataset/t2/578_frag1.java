    public void sendRemote(ChatMessage msg) {

        try {

            if (comm != null && isConnected()) {

                comm.sendMessage(msg);

            }

        } catch (Exception ex) {

            ExceptionHandler.handleException(ex);

        }

    }
