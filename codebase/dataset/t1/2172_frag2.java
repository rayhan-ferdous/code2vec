    private static final String[] QRLEVEL = { "PATIENT", "STUDY", "SERIES", "IMAGE" };



    private static ResourceBundle messages = ResourceBundle.getBundle("DcmDir", Locale.getDefault());



    private static final DcmObjectFactory dof = DcmObjectFactory.getInstance();



    private static final DirBuilderFactory fact = DirBuilderFactory.getInstance();



    private final TagDictionary dict = DictionaryFactory.getInstance().getDefaultTagDictionary();



    private File dirFile = null;



    private File readMeFile = null;



    private String readMeCharset = null;



    private boolean skipGroupLen = true;



    private boolean undefSeqLen = true;



    private boolean undefItemLen = true;



    private String id = "";



    private String uid = null;



    private Integer maxlen = new Integer(79);



    private Integer vallen = new Integer(64);



    private boolean onlyInUse = false;



    private boolean ignoreCase = false;



    private final Properties cfg;



    private final Dataset keys = dof.newDataset();



    private final int qrLevel;



    private static HashSet patientIDs = new HashSet();



    private static HashSet studyUIDs = new HashSet();



    private static HashSet seriesUIDs = new HashSet();



    private static HashSet sopInstUIDs = new HashSet();



    private LinkedList fileIDs = new LinkedList();



    private static final LongOpt[] LONG_OPTS = new LongOpt[] { new LongOpt("onlyInUse", LongOpt.NO_ARGUMENT, null, 3), new LongOpt("ignoreCase", LongOpt.NO_ARGUMENT, null, 3), new LongOpt("maxlen", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("vallen", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("fs-uid", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("fs-id", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("readme", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("readme-charset", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("grouplen", LongOpt.NO_ARGUMENT, null, 3), new LongOpt("seqlen", LongOpt.NO_ARGUMENT, null, 3), new LongOpt("itemlen", LongOpt.NO_ARGUMENT, null, 3), new LongOpt("pat", LongOpt.REQUIRED_ARGUMENT, null, 'p'), new LongOpt("study", LongOpt.REQUIRED_ARGUMENT, null, 's'), new LongOpt("series", LongOpt.REQUIRED_ARGUMENT, null, 'e'), new LongOpt("sop", LongOpt.REQUIRED_ARGUMENT, null, 'o'), new LongOpt("key", LongOpt.REQUIRED_ARGUMENT, null, 'y'), new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'), new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v') };



    /**

     *  Description of the Method

     *

     * @param  args           Description of the Parameter

     * @exception  Exception  Description of the Exception

     */

    public static void main(String args[]) throws Exception {

        Getopt g = new Getopt("dcmdir", args, "c:t:q:a:x:X:z:P:", LONG_OPTS);

        Properties cfg = loadConfig();

        int cmd = 0;

        File dirfile = null;

        int c;

        while ((c = g.getopt()) != -1) {

            switch(c) {

                case 2:

                    cfg.put(LONG_OPTS[g.getLongind()].getName(), g.getOptarg());

                    break;

                case 3:

                    cfg.put(LONG_OPTS[g.getLongind()].getName(), "<yes>");

                    break;

                case 'c':

                case 't':

                case 'q':

                case 'a':

                case 'x':

                case 'X':

                case 'P':

                case 'z':

                    cmd = c;

                    dirfile = new File(g.getOptarg());

                    break;

                case 'p':

                    patientIDs.add(g.getOptarg());

                    break;

                case 's':

                    studyUIDs.add(g.getOptarg());

                    break;

                case 'e':

                    seriesUIDs.add(g.getOptarg());

                    break;

                case 'o':

                    sopInstUIDs.add(g.getOptarg());

                    break;

                case 'y':

                    putKey(cfg, g.getOptarg());

                    break;

                case 'v':

                    exit(messages.getString("version"), false);

                case 'h':

                    exit(messages.getString("usage"), false);

                case '?':

                    exit(null, true);

                    break;

            }

        }

        if (cmd == 0) {

            exit(messages.getString("missing"), true);

        }

        try {

            DcmDir dcmdir = new DcmDir(dirfile, cfg);

            switch(cmd) {

                case 0:

                    exit(messages.getString("missing"), true);

                    break;

                case 'c':

                    dcmdir.create(args, g.getOptind());

                    break;

                case 't':

                    dcmdir.list();

                    break;

                case 'q':

                    dcmdir.query();

                    break;

                case 'a':

                    dcmdir.append(args, g.getOptind());

                    break;

                case 'x':

                case 'X':

                    dcmdir.remove(args, g.getOptind(), cmd == 'X');

                    break;

                case 'z':

                    dcmdir.compact();

                    break;

                case 'P':

                    dcmdir.purge();

                    break;

                default:

                    throw new RuntimeException();

            }

        } catch (IllegalArgumentException e) {

            e.printStackTrace();

            exit(e.getMessage(), true);

        }

    }



    private static void putKey(Properties cfg, String s) {

        int pos = s.indexOf(':');

        if (pos == -1) {

            cfg.put("key." + s, "");

        } else {

            cfg.put("key." + s.substring(0, pos), s.substring(pos + 1));

        }

    }



    DcmDir(File dirfile, Properties cfg) {

        this.dirFile = dirfile;

        this.cfg = cfg;

        String rm = replace(cfg.getProperty("readme"), "<none>", null);

        if (rm != null) {

            this.readMeFile = new File(rm);

            this.readMeCharset = replace(cfg.getProperty("readme-charset"), "<none>", null);

        }

        this.id = replace(cfg.getProperty("fs-id", ""), "<none>", "");

        this.uid = replace(cfg.getProperty("fs-uid", ""), "<auto>", "");

        this.maxlen = new Integer(cfg.getProperty("maxlen", "79"));

        this.vallen = new Integer(cfg.getProperty("maxlen", "64"));

        this.skipGroupLen = !"<yes>".equals(cfg.getProperty("grouplen"));

        this.undefSeqLen = !"<yes>".equals(cfg.getProperty("seqlen"));

        this.undefItemLen = !"<yes>".equals(cfg.getProperty("itemlen"));

        this.onlyInUse = "<yes>".equals(cfg.getProperty("onlyInUse"));

        this.ignoreCase = "<yes>".equals(cfg.getProperty("ignoreCase"));

        for (Enumeration it = cfg.keys(); it.hasMoreElements(); ) {

            String key = (String) it.nextElement();

            if (key.startsWith("key.")) {

                try {

                    keys.putXX(Tags.forName(key.substring(4)), cfg.getProperty(key));

                } catch (Exception e) {

                    throw new IllegalArgumentException("Illegal key - " + key + "=" + cfg.getProperty(key));

                }

            }

        }

        String qrl = keys.getString(Tags.QueryRetrieveLevel, QRLEVEL[1]);

        this.qrLevel = Arrays.asList(QRLEVEL).indexOf(qrl);

        if (qrLevel == -1) {

            throw new IllegalArgumentException("Illegal Query Retrieve Level - " + qrl);

        }

        keys.remove(Tags.QueryRetrieveLevel);

    }



    private TransformerHandler getTransformerHandler(SAXTransformerFactory tf, Templates tpl) throws TransformerConfigurationException, IOException {
