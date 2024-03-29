    public void setMatrix(int i0, int i1, int j0, int j1, Matrix X) throws Exception {

        try {

            for (int i = i0; i <= i1; i++) {

                for (int j = j0; j <= j1; j++) {

                    A[i][j] = X.get(i - i0, j - j0);

                }

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            throw new Exception("Submatrix indices");

        }

    }
