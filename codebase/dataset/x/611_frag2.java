    public synchronized void edgesChanged(GraphEvent event) {

        Rectangle2D bbox = null;

        Object objs[] = event.getObjects();

        for (int i = 0; i < objs.length; i++) {

            GraphEdgeModel edge = (GraphEdgeModel) objs[i];

            if (bbox == null) bbox = edge.getChangeBBox(); else bbox.add(edge.getChangeBBox());

        }

        if (model.isDirty()) repaint(model.getChangeBBox());

        repaint(bbox);

    }
