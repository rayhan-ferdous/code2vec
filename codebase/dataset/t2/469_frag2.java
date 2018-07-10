    void fireCtcpVersionReplyReceived(CtcpVersionReplyEvent event) {

        synchronized (ctcpListeners) {

            for (final CtcpListener ctcpListener : ctcpListeners) {

                try {

                    ctcpListener.versionReplyReceived(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }
