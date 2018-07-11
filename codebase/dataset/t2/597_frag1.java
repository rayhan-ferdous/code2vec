    public FloatMatrix getMatrix(int i0, int i1, int j0, int j1) {

        FloatMatrix X = new FloatMatrix(i1 - i0 + 1, j1 - j0 + 1);

        float[][] B = X.getArray();

        try {

            for (int i = i0; i <= i1; i++) {

                for (int j = j0; j <= j1; j++) {

                    B[i - i0][j - j0] = A[i][j];

                }

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            throw new ArrayIndexOutOfBoundsException("Submatrix indices");

        }

        return X;

    }
