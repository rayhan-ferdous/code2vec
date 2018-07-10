    public RealArray lowerTriangle() {

        int n = rows;

        RealArray triangle = new RealArray((n * (n + 1)) / 2);

        int count = 0;

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j <= i; j++) {

                triangle.setElementAt(count++, flmat[i][j]);

            }

        }

        return triangle;

    }
