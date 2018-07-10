    void fireWhoReceived(WhoEvent event) {

        synchronized (connectionListeners) {

            for (final ConnectionListener connectionListener : connectionListeners) {

                try {

                    connectionListener.whoReceived(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }
