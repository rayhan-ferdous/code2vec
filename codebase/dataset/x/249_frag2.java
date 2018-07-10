    public static void writeFile(Document doc, File file) throws IOException {

        TransformerFactory tFactory = TransformerFactory.newInstance();

        Transformer transformer;

        try {

            transformer = tFactory.newTransformer();

        } catch (TransformerConfigurationException tex) {

            throw new IOException(tex.getMessage());

        }

        DOMSource source = new DOMSource(doc);

        FileOutputStream fos = new FileOutputStream(file);

        StreamResult result = new StreamResult(fos);

        try {

            transformer.transform(source, result);

            fos.close();

        } catch (TransformerException tex) {

            throw new IOException(tex.getMessage());

        }

    }
