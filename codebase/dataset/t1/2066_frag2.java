    protected void setUp() throws Exception {

        super.setUp();

        props = new Properties();

        InputStream pin = getClass().getClassLoader().getResourceAsStream("test.mp3.properties");

        props.load(pin);

        basefile = (String) props.getProperty("basefile");

        baseurl = (String) props.getProperty("baseurl");

        name = (String) props.getProperty("filename");

        filename = basefile + name;

        fileurl = baseurl + name;

        out = System.out;

    }
