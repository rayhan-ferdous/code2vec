    public void testTwoDimensionalArrays() {

        final int rows = 3;

        final int cols = 4;

        int count = 0;

        int[][] matrix = new int[rows][cols];

        for (int x = 0; x < rows; x++) for (int y = 0; y < cols; y++) matrix[x][y] = count++;

        assertEquals(11, matrix[2][3]);

        assertEquals(6, matrix[1][2]);

    }
