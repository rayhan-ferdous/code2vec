    void fireNewMeetMeUser(MeetMeUser user) {

        synchronized (listeners) {

            for (AsteriskServerListener listener : listeners) {

                try {

                    listener.onNewMeetMeUser(user);

                } catch (Exception e) {

                    logger.warn("Exception in onNewMeetMeUser()", e);

                }

            }

        }

    }
