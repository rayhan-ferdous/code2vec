            byte0 = 5;

        } else {

            byte0 = 0;

        }

        return new Dimension(1 + c1._flddo + 2 * byte0, 1 + c1._fldif + c1.a + 2 * byte0);

    }



    public int getAscent(final String s1, final Graphics g1) {

        if (d != g1.getFont()) {

            d = g1.getFont();
