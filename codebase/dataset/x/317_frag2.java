    synchronized void fireCtcpErrmsgReplyReceived(CtcpErrmsgReplyEvent event) {

        for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {

            try {

                ((CtcpListener) it.next()).errmsgReplyReceived(event);

            } catch (Exception exc) {

                handleException(exc);

            }

        }

    }
