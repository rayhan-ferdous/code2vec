    public Matrix arrayRightDivideEquals(Matrix B) {

        checkMatrixDimensions(B);

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                A[i][j] = A[i][j] / B.A[i][j];

            }

        }

        return this;

    }
