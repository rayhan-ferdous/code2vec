    public final void setNodeHeight(double value, boolean adjustChildBranchLengths) {

        if (value < 0) {

            height = -value;

        } else {

            height = value;

        }

        if (adjustChildBranchLengths && child != null) {

            for (int i = 0; i < child.length; i++) {

                child[i].setBranchLength(height - child[i].getNodeHeight());

            }

        }

    }
