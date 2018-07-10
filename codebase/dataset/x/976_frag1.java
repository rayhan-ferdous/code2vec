    public void mouseExited(MouseEvent e) {

        if (!cCursor.getState()) return;

        if (vwins == null) return;

        ImagePlus imp;

        ImageWindow iw;

        ImageCanvas ic;

        Point p;

        Rectangle rect;

        p = new Point(x, y);

        rect = boundingRect(x, y, x, y);

        ImageCanvas icc = (ImageCanvas) e.getSource();

        ImageWindow iwc = (ImageWindow) icc.getParent();

        for (int n = 0; n < vwins.size(); ++n) {

            if (ijInstance.quitting()) {

                return;

            }

            imp = getImageFromVector(n);

            if (imp != null) {

                iw = imp.getWindow();

                ic = iw.getCanvas();

                if (cCoords.getState() && iw != iwc) {

                    p = getMatchingCoords(ic, icc, x, y);

                    rect = boundingRect(p.x, p.y, p.x, p.y);

                } else {

                    p.x = x;

                    p.y = y;

                    rect = boundingRect(x, y, x, y);

                }

                Graphics g = ic.getGraphics();

                try {

                    g.setClip(rect.x, rect.y, rect.width, rect.height);

                    ic.paint(g);

                } finally {

                    g.dispose();

                }

                if (iw != iwc) ic.mouseExited(adaptEvent(e, ic, p));

            }

        }

        storeCanvasState(icc);

    }
