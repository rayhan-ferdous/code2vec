    public static FloatMatrix identity(int m, int n) {

        FloatMatrix A = new FloatMatrix(m, n);

        float[][] X = A.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                X[i][j] = (float) (i == j ? 1.0 : 0.0);

            }

        }

        return A;

    }
