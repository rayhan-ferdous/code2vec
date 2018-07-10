    public static Matrix random(int m, int n) {

        Matrix A = new Matrix(m, n);

        double[][] X = A.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                X[i][j] = Math.random();

            }

        }

        return A;

    }
