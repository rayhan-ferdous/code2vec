    public static boolean hasAlpha(Image image) {

        if (image instanceof BufferedImage) {

            return ((BufferedImage) image).getColorModel().hasAlpha();

        }

        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);

        try {

            pg.grabPixels();

        } catch (InterruptedException e) {

        }

        return pg.getColorModel().hasAlpha();

    }
