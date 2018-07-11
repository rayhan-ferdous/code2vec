        rect = boundingRect(x, y, oldX, oldY);

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
