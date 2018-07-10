        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = dbf.newDocumentBuilder();

        DOMImplementation di = db.getDOMImplementation();

        Document doc = di.createDocument("", "buttons", null);

        Element root = doc.getDocumentElement();

        root.setAttribute("start", start);
