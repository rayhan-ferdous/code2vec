        Vector outV = new Vector();

        NodeList nl = getNodes(appId, nodeId);

        for (int i = 0; i < nl.getLength(); i++) {

            Element storyElem = null;

            Node node = nl.item(i);

            if (node != null && node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().toLowerCase().equals("headline")) {
