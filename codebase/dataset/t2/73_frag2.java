    void firePrivateCtcpDccSendReceived(CtcpDccSendEvent event) {

        synchronized (privateCtcpListeners) {

            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {

                try {

                    privateCtcpListener.dccSendReceived(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }
