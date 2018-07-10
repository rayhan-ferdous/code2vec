        public synchronized void lines() {

            for (int i = pixels.length - 1; i >= 0; i--) {

                pixels[i] = 0;

                pixelsZ[i] = 1000;

            }

            for (int row = 0; row < 255; row++) {

                for (int col = 0; col < 255; col++) {

                    int i = row * 256 + col;

                    PlotVal p0 = plotList[i];
