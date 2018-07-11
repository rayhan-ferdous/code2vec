    protected final void synchronizeChildren(AttrImpl a, int nodeIndex) {

        boolean orig = getMutationEvents();

        setMutationEvents(false);

        a.needsSyncChildren(false);

        int last = getLastChild(nodeIndex);

        int prev = getPrevSibling(last);

        if (prev == -1) {

            a.value = getNodeValueString(nodeIndex);

            a.hasStringValue(true);

        } else {

            ChildNode firstNode = null;

            ChildNode lastNode = null;

            for (int index = last; index != -1; index = getPrevSibling(index)) {

                ChildNode node = (ChildNode) getNodeObject(index);

                if (lastNode == null) {

                    lastNode = node;

                } else {

                    firstNode.previousSibling = node;

                }

                node.ownerNode = a;

                node.isOwned(true);

                node.nextSibling = firstNode;

                firstNode = node;

            }

            if (lastNode != null) {

                a.value = firstNode;

                firstNode.isFirstChild(true);

                a.lastChild(lastNode);

            }

            a.hasStringValue(false);

        }

        setMutationEvents(orig);

    }
