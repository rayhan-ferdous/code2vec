    public static OutputStream documentToOutputStream(Document doc) {

        if (doc == null) return null;

        OutputStream out = new ByteArrayOutputStream();

        OutputFormat outputFormat = new OutputFormat("xml", "UTF-8", true);

        XMLSerializer serializer = new XMLSerializer(outputFormat);

        serializer.setNamespaces(true);

        serializer.setOutputByteStream(out);

        try {

            serializer.serialize(doc);

        } catch (IOException e) {

            String message = "couldn't serialize document";

            System.out.println(message);

        }

        return out;

    }
