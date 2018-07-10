    public void saveChannels(StringBuffer output) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = dbf.newDocumentBuilder();

        DOMImplementation di = db.getDOMImplementation();

        Document doc = di.createDocument("", "channels", null);

        Element root = doc.getDocumentElement();

        String[] keys = (String[]) channels.keySet().toArray(new String[0]);

        for (int x = 0; x < keys.length; x++) {

            Channel chan = (Channel) channels.get(keys[x]);

            root.appendChild(chan.getXML(doc));

        }

        ByteArrayOutputStream buff = new ByteArrayOutputStream();

        TransformerFactory factory = TransformerFactory.newInstance();

        Transformer transformer = factory.newTransformer();

        transformer.setOutputProperty(OutputKeys.METHOD, "xml");

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        Source source = new DOMSource(doc);

        Result result = new StreamResult(buff);

        transformer.transform(source, result);

        if (output != null) {

            output.append(buff.toString());

        } else {

            FileWriter out = new FileWriter(this.getProperty("path.data") + File.separator + "Channels.xml");

            out.write(buff.toString());

            out.close();

            System.out.println("Channels.xml saved.");

        }

    }
