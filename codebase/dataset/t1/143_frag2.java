    public long getLong(int[] index) {

        if (index.length < rank) throw new IllegalArgumentException();

        final int end = rank - 1;

        Object oo = jla;

        for (int ii = 0; ii < end; ii++) oo = Array.get(oo, index[ii]);
