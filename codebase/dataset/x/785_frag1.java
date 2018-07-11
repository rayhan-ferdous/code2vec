        return node.isLeafNode();

    }



    public void setParentNode(BTreeNode node0) throws DException {

        node.setParentNode(node0);

    }



    public void updateChild(int position, Object key) throws DException {

        node.updateChild(position, key);

    }



    public _Node getNode() {

        return node;
