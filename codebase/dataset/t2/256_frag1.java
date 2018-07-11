    void fireUserModeReceived(UserModeEvent event) {

        synchronized (connectionListeners) {

            for (final ConnectionListener connectionListener : connectionListeners) {

                try {

                    connectionListener.userModeReceived(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }
