    public CMatrix timesEquals(Complex s) {

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                A[i][j] = s.multiply(A[i][j]);

            }

        }

        return this;

    }
