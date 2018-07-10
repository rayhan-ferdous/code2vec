    public CMatrix getMatrix(int[] r, int j0, int j1) {

        CMatrix X = new CMatrix(r.length, j1 - j0 + 1);

        Complex[][] B = X.getArray();

        try {

            for (int i = 0; i < r.length; i++) {

                for (int j = j0; j <= j1; j++) {

                    B[i][j - j0] = A[r[i]][j];

                }

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            throw new ArrayIndexOutOfBoundsException("Submatrix indices");

        }

        return X;

    }
