public class TemplateRunner {



    private final Set<String> reservedWords = new HashSet<String>();



    private final Set<String> parameters = new HashSet<String>();



    private PrintStream out;



    private final OutputType outputType;



    private String lastVariableName = "";



    private Map<String, String> reservedAttributeMap = new HashMap<String, String>();



    private Map<String, String> reservedStyleMap = new HashMap<String, String>();



    private boolean forceOverwrite;



    public TemplateRunner() {

        outputType = OutputType.load("min");

        reservedWords.add("var");

        reservedWords.add("do");

        reservedWords.add("if");

        reservedWords.add("in");

        reservedWords.add("for");

        reservedWords.add("class");

        reservedWords.add("float");

        reservedWords.addAll(Arrays.asList(outputType.getReservedWords()));

        reservedAttributeMap.put("for", "htmlFor");

        reservedAttributeMap.put("class", "className");

        reservedAttributeMap.put("rowspan", "rowSpan");

        reservedAttributeMap.put("colspan", "colSpan");

        reservedStyleMap.put("float", "cssFloat");

    }



    public void setForceOverwrite(boolean forceOverwrite) {

        this.forceOverwrite = forceOverwrite;

    }



    public void run(String inputFolderName, String outputFolderName) {

        File inputFolder = new File(inputFolderName);

        if (!inputFolder.isDirectory()) {

            throw new DCTemplateException("Input folder is not a directory: " + inputFolderName);

        }

        File[] files = inputFolder.listFiles();

        if (files != null) {

            for (int i = 0; i < files.length; i++) {

                if (files[i].getName().endsWith(".js.xml")) {

                    run(files[i], outputFolderName);

                }

            }

        }

    }



    public void run(File templateFile, String outputFolder) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(templateFile);

            Element templateElement = doc.getDocumentElement();

            String templateName = templateElement.getAttribute("name");

            if (templateName.length() == 0) {

                throw new DCTemplateException("'template' tag requires the 'name' attribute");

            }

            String dataName = templateElement.getAttribute("data");

            String[] parameters = dataName.split(",\\s*");

            this.parameters.clear();

            for (String parameter : parameters) {

                for (String outputReserved : reservedWords) {

                    if (parameter.equals(outputReserved)) {

                        throw new DCTemplateException("'" + outputReserved + "' is a reserved variable and cannot be used in the template 'data' attribute");

                    }

                }

                this.parameters.add(parameter);

            }

            try {

                File outFile = new File(outputFolder + "/" + templateName + ".js");

                if (!forceOverwrite && outFile.exists() && outFile.lastModified() > templateFile.lastModified()) {

                    Logger.getLogger(TemplateRunner.class.getName()).log(Level.INFO, "skipping " + templateFile.getName() + " as output file is newer, forceOveride is false");
