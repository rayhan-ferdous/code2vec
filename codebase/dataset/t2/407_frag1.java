    void fireChannelJoined(UserParticipationEvent event) {

        synchronized (connectionListeners) {

            for (final ConnectionListener connectionListener : connectionListeners) {

                try {

                    connectionListener.channelJoined(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }
