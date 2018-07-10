        final Graphics2D g = bi.createGraphics();

        final GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, width, height, getRandColor(120, 200), false);

        g.setPaint(gp);

        g.fillRect(0, 0, width, height);
