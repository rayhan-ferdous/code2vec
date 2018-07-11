    private PlayerListPopupMenu popup;



    public String name;



    public boolean isLaunchable;



    private ChannelStatusListCellRenderer renderer;



    /** Creates new form ChannelPanel */

    public ChannelPanel(String name) {

        this.name = name;

        id = GameDatabase.getIDofGame(name);

        users = new ChannelStatusListModel();

        renderer = new ChannelStatusListCellRenderer(users);
