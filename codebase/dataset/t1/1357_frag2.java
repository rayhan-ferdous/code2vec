    protected void getImagePixels() {

        int w = image.getWidth();

        int h = image.getHeight();

        int type = image.getType();

        if ((w != width) || (h != height) || (type != BufferedImage.TYPE_3BYTE_BGR)) {

            BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

            Graphics2D g = temp.createGraphics();

            g.drawImage(image, 0, 0, null);

            image = temp;

        }

        pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

    }
