    private static void sendBulkMessage(String command, String data) {

        for (Map.Entry<String, OldConvergiaServer.ConnectionHandler> entry : userConnections.entrySet()) {

            ConnectionHandler h = entry.getValue();

            try {

                h.sendTextGroup(command, data);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }
