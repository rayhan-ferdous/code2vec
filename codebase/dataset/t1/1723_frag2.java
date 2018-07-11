    protected void drawTile(Node node, Graphics g, JInternalFrame jif) {

        NamedNodeMap attrs = node.getAttributes();

        int x0 = aee.evaluate(getStringAttr(attrs, "x"));

        int y0 = aee.evaluate(getStringAttr(attrs, "y"));

        int w = aee.evaluate(getStringAttr(attrs, "width"));

        int h = aee.evaluate(getStringAttr(attrs, "height"));

        int tw = aee.evaluate(getStringAttr(attrs, "tile_width"));

        int th = aee.evaluate(getStringAttr(attrs, "tile_height"));

        int width = getInt("width");

        int height = getInt("height");

        if (width == -1) {

            x0 -= w;

        }

        if (height == -1) {

            y0 -= h;

        }

        Shape oldClip = g.getClip();

        if (g instanceof Graphics2D) {

            ((Graphics2D) g).clip(new Rectangle(x0, y0, w, h));

        }

        variables.put("width", tw);

        variables.put("height", th);

        Node draw_ops = getNode("draw_ops", new String[] { "name", getStringAttr(node, "name") });

        int y = y0;

        while (y < y0 + h) {

            int x = x0;

            while (x < x0 + w) {

                g.translate(x, y);

                draw(draw_ops, g, jif);

                g.translate(-x, -y);

                x += tw;

            }

            y += th;

        }

        variables.put("width", width);

        variables.put("height", height);

        g.setClip(oldClip);

    }
