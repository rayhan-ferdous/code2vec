    public float normInf() {

        float f = 0;

        for (int i = 0; i < m; i++) {

            float s = 0;

            for (int j = 0; j < n; j++) {

                s += Math.abs(A[i][j]);

            }

            f = Math.max(f, s);

        }

        return f;

    }
