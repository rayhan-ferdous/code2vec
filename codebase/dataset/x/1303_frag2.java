    public double normF() {

        double f = 0;

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                f = Maths.hypot(f, A[i][j]);

            }

        }

        return f;

    }
