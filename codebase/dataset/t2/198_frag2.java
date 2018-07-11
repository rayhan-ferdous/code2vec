    void firePrivateCtcpSourceRequestReceived(CtcpSourceRequestEvent event) {

        synchronized (privateCtcpListeners) {

            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {

                try {

                    privateCtcpListener.sourceRequestReceived(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }
