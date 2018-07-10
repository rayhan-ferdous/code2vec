    public Matrix getMatrix(int[] r, int[] c) {

        Matrix X = new Matrix(r.length, c.length);

        double[][] B = X.getArray();

        try {

            for (int i = 0; i < r.length; i++) {

                for (int j = 0; j < c.length; j++) {

                    B[i][j] = A[r[i]][c[j]];

                }

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            throw new ArrayIndexOutOfBoundsException("Submatrix indices");

        }

        return X;

    }
