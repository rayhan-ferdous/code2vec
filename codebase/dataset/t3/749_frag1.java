    void fireInvitationReceived(InvitationEvent event) {

        synchronized (connectionListeners) {

            for (final ConnectionListener connectionListener : connectionListeners) {

                try {

                    connectionListener.invitationReceived(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }



    void fireInvitationDeliveryReceived(InvitationEvent event) {
