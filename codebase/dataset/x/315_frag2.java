        CallableStatement statement = null;

        try {

            statement = sqlConnection.prepareCall(SEND_MESSAGE_QUERY);

            statement.setString(1, message.getChannel());

            statement.setString(2, message.getMessage());

            statement.setString(3, message.getAuteur());
