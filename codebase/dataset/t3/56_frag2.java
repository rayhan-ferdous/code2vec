    public Matrix ebePow(double p) {

        Matrix X = new Matrix(m, n);

        double[][] C = X.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                C[i][j] = Math.pow(A[i][j], p);

            }

        }

        return X;

    }
