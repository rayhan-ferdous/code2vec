    public void setMatrix(int[] r, int j0, int j1, Matrix X) {

        try {

            for (int i = 0; i < r.length; i++) {

                for (int j = j0; j <= j1; j++) {

                    A[r[i]][j] = X.get(i, j - j0);

                }

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
