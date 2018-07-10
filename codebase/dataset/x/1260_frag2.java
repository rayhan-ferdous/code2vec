    public void doRemoveVertex() {

        if (currVertex != -1) {

            try {

                addUndo();

                currGraph.removeVertex(currVertex);

                currVertex = -1;

                repaint();

            } catch (IllegalVertexException e) {

                System.out.println("Check error in GPanel.doRemoveVertex");

            }

        }

    }
