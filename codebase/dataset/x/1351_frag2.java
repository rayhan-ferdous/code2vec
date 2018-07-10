    public void updateSlice(final int z) {

        Thread r = new Thread() {



            public void run() {

                if (z < 0 || z > depth) return;

                depthSlider.setValue(z);

            }

        };

        try {

            SwingUtilities.invokeAndWait(r);

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (InvocationTargetException e) {

            e.printStackTrace();

        }

        if (!isMinimized()) updateImage();

    }
