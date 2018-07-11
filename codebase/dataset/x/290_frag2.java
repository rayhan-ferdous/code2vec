    IRC_Message(IRC_Connection conn, IRCUser sender, String channel, boolean isAddressed, String message) {

        this.my_conn = conn;

        this.my_sender = sender;

        this.my_channel = channel;

        this.my_isAddressed = isAddressed;

        this.my_message = message;

    }
