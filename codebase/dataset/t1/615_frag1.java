    public void testArrayEquality() {

        int[] a = { 1, 2, 3 };

        int[] b = { 1, 2, 3 };

        assertFalse(a.equals(b));

        assertFalse(a == b);

    }
