        NodeList nList = n.getChildNodes();

        for (int i = 0; i < nList.getLength(); i++) {

            Node n1 = nList.item(i);

            if (n1.getNodeType() == Node.ELEMENT_NODE) {

                Element e1 = (Element) n1;

                String name = n1.getNodeName();

                if (name.equals("case")) {
