    public CommandHandler commandHandler;



    private String ircServer;



    private int ircServerPort;



    private String ircName;



    private String ircChannel;



    private String ircCrewChannel;



    private String ircCrewChannelPassword;



    private String ircBindAddress;



    public Handler() {

        commandHandler = new CommandHandler(this);

        ircServer = Main.getInstance().getConfigHandler().ircNetwork;
