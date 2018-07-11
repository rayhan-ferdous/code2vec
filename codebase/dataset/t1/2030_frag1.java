    public static void exampleFromStream(String sourceID, String xslID) throws TransformerException, TransformerConfigurationException, FileNotFoundException {

        TransformerFactory tfactory = TransformerFactory.newInstance();

        InputStream xslIS = new BufferedInputStream(new FileInputStream(xslID));

        StreamSource xslSource = new StreamSource(xslIS);

        xslSource.setSystemId(xslID);

        Transformer transformer = tfactory.newTransformer(xslSource);

        InputStream xmlIS = new BufferedInputStream(new FileInputStream(sourceID));

        StreamSource xmlSource = new StreamSource(xmlIS);

        xmlSource.setSystemId(sourceID);

        transformer.transform(xmlSource, new StreamResult(new OutputStreamWriter(System.out)));

    }
