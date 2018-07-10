                    A[i][j] = X.get(i - i0, j - j0);

                }

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            throw new ArrayIndexOutOfBoundsException("Submatrix indices");

        }

    }



    /** Set a submatrix.

   @param r    Array of row indices.

   @param c    Array of column indices.

   @param X    A(r(:),c(:))

   @exception  ArrayIndexOutOfBoundsException Submatrix indices

   */

    public void setMatrix(int[] r, int[] c, Matrix X) {

        try {

            for (int i = 0; i < r.length; i++) {

                for (int j = 0; j < c.length; j++) {
