    public double trace() {

        double t = 0;

        for (int i = 0; i < Math.min(m, n); i++) {

            t += A[i][i];

        }

        return t;

    }



    /**

	 * Generate matrix with random elements

	 * 

	 * @param m

	 *            Number of rows.

	 * @param n

	 *            Number of colums.

	 * @return An m-by-n matrix with uniformly distributed random elements.

	 */

    public static Matrix random(int m, int n) {

        Matrix A = new Matrix(m, n);

        double[][] X = A.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                X[i][j] = Math.random();

            }

        }

        return A;

    }



    /**

	 * Generate identity matrix

	 * 

	 * @param m

	 *            Number of rows.

	 * @param n

	 *            Number of colums.

	 * @return An m-by-n matrix with ones on the diagonal and zeros elsewhere.

	 */

    public static Matrix identity(int m, int n) {

        Matrix A = new Matrix(m, n);

        double[][] X = A.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                X[i][j] = (i == j ? 1.0 : 0.0);

            }

        }

        return A;

    }



    /**

	 * Print the matrix to stdout. Line the elements up in columns with a

	 * Fortran-like 'Fw.d' style format.

	 * 

	 * @param w

	 *            Column width.

	 * @param d

	 *            Number of digits after the decimal.

	 */

    public void print(int w, int d) {
