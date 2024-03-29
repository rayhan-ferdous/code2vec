    public FloatMatrix arrayRightDivide(FloatMatrix B) {

        checkMatrixDimensions(B);

        FloatMatrix X = new FloatMatrix(m, n);

        float[][] C = X.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                C[i][j] = A[i][j] / B.A[i][j];

            }

        }

        return X;

    }
