    public NewMessageEvent(Object source, String hostmask, String user, String channel, String message) {

        super(source);

        this.hostmask = hostmask;

        this.user = user;

        this.channel = channel;

        this.message = message;

    }
