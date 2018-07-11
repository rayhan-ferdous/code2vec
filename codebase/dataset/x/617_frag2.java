        BufferedImage bimage = new BufferedImage(pdfWidth, pdfHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = bimage.createGraphics();

        g.setClip(0, 0, pdfWidth, pdfHeight);

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
