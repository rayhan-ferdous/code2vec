    public Matrix add(Matrix m) {

        Matrix result = new Matrix(height, width);

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                result.elements[i][j] = elements[i][j] + m.elements[i][j];

            }

        }

        return result;

    }
