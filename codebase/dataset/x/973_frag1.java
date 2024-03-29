    synchronized void fireCtcpClientinfoReplyReceived(CtcpClientinfoReplyEvent event) {

        for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {

            try {

                ((CtcpListener) it.next()).clientinfoReplyReceived(event);

            } catch (Exception exc) {

                handleException(exc);

            }

        }

    }
