    void fireNumericErrorReceived(NumericEvent event) {

        synchronized (connectionListeners) {

            for (final ConnectionListener connectionListener : connectionListeners) {

                try {

                    connectionListener.numericErrorReceived(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }
