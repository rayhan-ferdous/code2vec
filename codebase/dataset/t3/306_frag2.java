    public void saveEpgWatchList(StringBuffer output) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = dbf.newDocumentBuilder();

        DOMImplementation di = db.getDOMImplementation();

        Document doc = di.createDocument("", "watch-list", null);

        Element root = doc.getDocumentElement();

        for (int x = 0; x < epgWatchList.size(); x++) {

            Element item = doc.createElement("item");

            Text text = doc.createTextNode(epgWatchList.get(x));

            item.appendChild(text);

            root.appendChild(item);

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

            FileWriter out = new FileWriter(this.getProperty("path.data") + File.separator + "EpgWatchList.xml");

            out.write(buff.toString());

            out.close();

            System.out.println("EpgWatchList.xml saved.");

        }

    }
