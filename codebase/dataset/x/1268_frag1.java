    void updateContours() {

        Transposer transposer = getTransposer();

        branch.validate();

        cachedContourLeft = new int[getDepth()];

        cachedContourRight = new int[getDepth()];

        Rectangle clientArea = transposer.t(branch.getNodeBounds().getUnion(branch.contents.getBounds()));

        Rectangle nodeBounds = transposer.t(branch.getNodeBounds());

        Rectangle contentsBounds = transposer.t(branch.getContentsPane().getBounds());

        cachedContourLeft[0] = nodeBounds.x - clientArea.x;

        cachedContourRight[0] = clientArea.right() - nodeBounds.right();

        if (!branch.isExpanded()) return;

        List subtrees = getSubtrees();

        TreeBranch subtree;

        int currentDepth = 0;

        for (int i = 0; i < subtrees.size() && currentDepth < getDepth(); i++) {

            subtree = (TreeBranch) subtrees.get(i);

            if (subtree.getDepth() > currentDepth) {

                int leftContour[] = subtree.getContourLeft();

                int leftOffset = transposer.t(subtree.getBounds()).x - clientArea.x;

                mergeContour(cachedContourLeft, leftContour, currentDepth, leftOffset);

                currentDepth = subtree.getDepth();

            }

        }

        currentDepth = 0;

        for (int i = subtrees.size() - 1; i >= 0 && currentDepth < getDepth(); i--) {

            subtree = (TreeBranch) subtrees.get(i);

            if (subtree.getDepth() > currentDepth) {

                int rightContour[] = subtree.getContourRight();

                int rightOffset = clientArea.right() - transposer.t(subtree.getBounds()).right();

                mergeContour(cachedContourRight, rightContour, currentDepth, rightOffset);

                currentDepth = subtree.getDepth();

            }

        }

    }
