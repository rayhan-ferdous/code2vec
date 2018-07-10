    void fireErrorReceived(ErrorEvent event) {

        synchronized (connectionListeners) {

            for (final ConnectionListener connectionListener : connectionListeners) {

                try {

                    connectionListener.errorReceived(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }
