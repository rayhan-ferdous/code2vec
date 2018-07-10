    public Matrix(double[] vals, int m) {

        this.m = m;

        n = ((m != 0) ? (vals.length / m) : 0);

        if ((m * n) != vals.length) {

            throw new IllegalArgumentException("Array length must be a multiple of " + m);

        }

        A = new double[m][n];

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                A[i][j] = vals[i + (j * m)];

            }

        }

    }
