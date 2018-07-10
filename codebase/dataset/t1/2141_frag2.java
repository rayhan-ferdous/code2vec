    public Matrix getMatrix(int i0, int i1, int[] c) {

        Matrix X = new Matrix(i1 - i0 + 1, c.length);

        double[][] B = X.getArray();

        try {

            for (int i = i0; i <= i1; i++) {

                for (int j = 0; j < c.length; j++) {

                    B[i - i0][j] = A[i][c[j]];

                }

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            throw new ArrayIndexOutOfBoundsException("Submatrix indices");

        }

        return X;

    }
