        checkMatrixDimensions(B);

        Matrix X = new Matrix(m, n);

        double[][] C = X.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {
