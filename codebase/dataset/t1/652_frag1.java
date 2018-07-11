    public Matrix(int m, int n, double s) {

        this.m = m;

        this.n = n;

        A = new double[m][n];

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                A[i][j] = s;

            }

        }

    }
