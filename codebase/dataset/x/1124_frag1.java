    public Matrix maxRows() {

        Matrix X = new Matrix(1, n);

        double[][] C = X.getArray();

        for (int j = 0; j < n; j++) {

            double maxval = A[0][j];

            for (int i = 0; i < m; i++) {

                maxval = Math.max(maxval, A[i][j]);

            }

            C[0][j] = maxval;

        }

        return X;

    }
