        System.out.println();

    }



    /**

     * Recurse through all of the XML nodes and output them.

     * @param node

     * @param indent 

     */

    public void printNode(Node node, String indent) {

        boolean nodeChildFound = false;

        switch(node.getNodeType()) {

            case Node.DOCUMENT_NODE:

                System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

                NodeList nodes = node.getChildNodes();

                if (nodes != null) {

                    for (int i = 0; i < nodes.getLength(); i++) {
