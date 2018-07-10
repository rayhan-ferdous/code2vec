    public void kick() {

        try {

            user.kick();

        } catch (ManagerCommunicationException e) {

            log.error("Failed to kick participant: " + user.getUserNumber() + " due to '" + e.getMessage() + "'");

            e.printStackTrace();

        }

    }
