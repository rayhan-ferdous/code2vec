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
