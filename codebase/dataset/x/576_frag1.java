    public void handleNotification(Notification msg, Object handback) {

        String type = msg.getType();

        if (type.equals(Server.START_NOTIFICATION_TYPE)) {

            log.debug("Saw " + type + " notification, starting connectors");

            try {

                startConnectors();

            } catch (Exception e) {

                log.warn("Failed to startConnectors", e);

            }

        }

    }
