    public double sum() {

        double sum = 0.0;

        for (int r = 0; r < rows; r++) {

            for (int c = 0; c < columns; c++) {

                sum += coefficients[r][c];

            }

        }

        return sum;

    }
