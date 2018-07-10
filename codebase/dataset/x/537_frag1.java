        Graphics graphics = getGraphics();

        boolean handled = false;

        if ((_zoomin == true) && (_drawn == true)) {

            if (_zoomxn != -1 || _zoomyn != -1) {

                int minx = Math.min(_zoomx, _zoomxn);

                int maxx = Math.max(_zoomx, _zoomxn);
