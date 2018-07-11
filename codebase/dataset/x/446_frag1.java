        Vector outV = new Vector();

        NodeList nl = getNodes(appId, nodeId);

        if (nl != null) {

            for (int i = 0; i < nl.getLength(); i++) {

                Node node = nl.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
