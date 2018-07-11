            try {

                javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();

                factory.setNamespaceAware(true);

                javax.xml.parsers.SAXParser jaxpParser = factory.newSAXParser();

                reader = jaxpParser.getXMLReader();

            } catch (javax.xml.parsers.ParserConfigurationException ex) {

                throw new org.xml.sax.SAXException(ex);

            } catch (javax.xml.parsers.FactoryConfigurationError ex1) {

                throw new org.xml.sax.SAXException(ex1.toString());

            } catch (NoSuchMethodError ex2) {

            }

            if (reader == null) reader = XMLReaderFactory.createXMLReader();

            reader.setContentHandler(handler);

            reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);

            reader.parse(sourceID);
