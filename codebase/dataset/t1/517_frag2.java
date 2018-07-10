    public void setMatrix(int[] r, int[] c, Matrix X) {

        try {

            for (int i = 0; i < r.length; i++) {

                for (int j = 0; j < c.length; j++) {

                    A[r[i]][c[j]] = X.get(i, j);

                }

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            throw new ArrayIndexOutOfBoundsException("Submatrix indices");

        }

    }
