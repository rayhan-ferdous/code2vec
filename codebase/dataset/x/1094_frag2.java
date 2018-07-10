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
