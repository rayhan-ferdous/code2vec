    public Curve getSubCurve(double ystart, double yend, int dir) {

        if (ystart <= y0 && yend >= y1) {

            return getWithDirection(dir);

        }

        double eqn[] = new double[14];

        double t0, t1;

        t0 = TforY(ystart);

        t1 = TforY(yend);

        eqn[0] = x0;

        eqn[1] = y0;

        eqn[2] = cx0;

        eqn[3] = cy0;

        eqn[4] = cx1;

        eqn[5] = cy1;

        eqn[6] = x1;

        eqn[7] = y1;

        if (t0 > t1) {

            double t = t0;

            t0 = t1;

            t1 = t;

        }

        if (t1 < 1) {

            split(eqn, 0, t1);

        }

        int i;

        if (t0 <= 0) {

            i = 0;

        } else {

            split(eqn, 0, t0 / t1);

            i = 6;

        }

        return new Order3(eqn[i + 0], ystart, eqn[i + 2], eqn[i + 3], eqn[i + 4], eqn[i + 5], eqn[i + 6], yend, dir);

    }
