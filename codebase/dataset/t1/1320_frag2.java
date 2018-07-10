    public void testPartialDimensions() {

        final int rows = 3;

        int[][] matrix = new int[rows][];

        matrix[0] = new int[] { 0 };

        matrix[1] = new int[] { 1, 2 };

        matrix[2] = new int[] { 3, 4, 5 };

        assertEquals(1, matrix[1][0]);

        assertEquals(5, matrix[2][2]);

        int[][] matrix2 = { { 0 }, { 1, 2 }, { 3, 4, 5 } };

        assertEquals(1, matrix2[1][0]);

        assertEquals(5, matrix2[2][2]);

        int[] z1 = { 3, 4, 5 };

        int[] z2 = { 3, 4, 5 };

        assertTrue(Arrays.equals(z1, z2));

    }
