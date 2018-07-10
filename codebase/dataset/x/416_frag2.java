    public static FloatMatrix random(int m, int n) {

        FloatMatrix A = new FloatMatrix(m, n);

        float[][] X = A.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                X[i][j] = (float) Math.random();

            }

        }

        return A;

    }
