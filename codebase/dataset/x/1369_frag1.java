    public void setLayout(InputSource document) throws SAXException, IOException {

        SAXParserFactory spf = SAXParserFactory.newInstance();

        spf.setValidating(false);

        XMLReader xmlReader = null;

        SAXParser saxParser;

        try {

            saxParser = spf.newSAXParser();

        } catch (ParserConfigurationException e) {

            throw new IOException("Couldn't load parser");

        }

        xmlReader = saxParser.getXMLReader();

        LayoutBuilder layoutBuilder = new LayoutBuilder();

        xmlReader.setContentHandler(layoutBuilder);

        resolver.startParseNotify();

        xmlReader.parse(document);

        resolver.endParseNotify();

        root = layoutBuilder.getRoot();

        reload(root);

    }
