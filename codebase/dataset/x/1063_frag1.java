    public VisualizerFrame() {

        super("Viewer");

        log.debug("Constructing VisualizerFrame");

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(this);

        this.addComponentListener(this);

        content = this.getContentPane();

        content.setLayout(new BorderLayout());

        glass = (JPanel) getGlassPane();

        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        this.setWindowName("Viewer_" + sdf.format(date));

        waitPanel = new WaitPanel();

        idSet = new UniqueIDSet();

    }
