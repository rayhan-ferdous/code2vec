    protected int findClosest(Color c) {

        if (colorTab == null) return -1;

        int r = c.getRed();

        int g = c.getGreen();

        int b = c.getBlue();

        int minpos = 0;

        int dmin = 256 * 256 * 256;

        int len = colorTab.length;

        for (int i = 0; i < len; ) {

            int dr = r - (colorTab[i++] & 0xff);

            int dg = g - (colorTab[i++] & 0xff);

            int db = b - (colorTab[i] & 0xff);

            int d = dr * dr + dg * dg + db * db;

            int index = i / 3;

            if (usedEntry[index] && (d < dmin)) {

                dmin = d;

                minpos = index;

            }

            i++;

        }

        return minpos;

    }
