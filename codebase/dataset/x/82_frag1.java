        float[][] C = X.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                C[i][j] = A[i][j] - B.A[i][j];

            }

        }

        return X;

    }



    /** A = A - B

     * @param B    another matrix

     * @return     A - B

     */

    public FloatMatrix minusEquals(FloatMatrix B) {

        checkMatrixDimensions(B);

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                A[i][j] = A[i][j] - B.A[i][j];

            }

        }

        return this;

    }



    /** Element-by-element multiplication, C = A.*B

     * @param B    another matrix

     * @return     A.*B

     */

    public FloatMatrix arrayTimes(FloatMatrix B) {

        checkMatrixDimensions(B);

        FloatMatrix X = new FloatMatrix(m, n);
