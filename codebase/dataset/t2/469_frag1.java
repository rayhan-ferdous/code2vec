    void fireWhoisReceived(WhoisEvent event) {

        synchronized (connectionListeners) {

            for (final ConnectionListener connectionListener : connectionListeners) {

                try {

                    connectionListener.whoisReceived(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }
