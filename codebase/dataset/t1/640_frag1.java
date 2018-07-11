    private static Templates loadDcmDirXSL(SAXTransformerFactory tf) throws IOException, TransformerConfigurationException {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        InputStream in = cl.getResourceAsStream("Dcm2Xml2.xsl");

        return tf.newTemplates(new StreamSource(in));

    }



    private static final DecimalFormat POS_FORMAT = new DecimalFormat("0000 DIRECTORY RECORD - ");



    private void list(String prefix, DirRecord first, SAXTransformerFactory tf, Templates xslt) throws IOException, TransformerConfigurationException {
