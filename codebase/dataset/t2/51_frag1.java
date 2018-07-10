    private static final void binop(SparseBitSet a, SparseBitSet b, BinOp op) {

        int nsize = a.size + b.size;

        long[] nbits;

        int[] noffs;

        int a_zero, a_size;

        if (a.bits.length < nsize) {

            nbits = new long[nsize];

            noffs = new int[nsize];

            a_zero = 0;

            a_size = a.size;

        } else {

            nbits = a.bits;

            noffs = a.offs;

            a_zero = a.bits.length - a.size;

            a_size = a.bits.length;

            System.arraycopy(a.bits, 0, a.bits, a_zero, a.size);

            System.arraycopy(a.offs, 0, a.offs, a_zero, a.size);

        }

        nsize = 0;

        for (int i = a_zero, j = 0; i < a_size || j < b.size; ) {

            long nb;

            int no;

            if (i < a_size && (j >= b.size || a.offs[i] < b.offs[j])) {

                nb = op.op(a.bits[i], 0);

                no = a.offs[i];

                i++;

            } else if (j < b.size && (i >= a_size || a.offs[i] > b.offs[j])) {

                nb = op.op(0, b.bits[j]);

                no = b.offs[j];

                j++;

            } else {

                nb = op.op(a.bits[i], b.bits[j]);

                no = a.offs[i];

                i++;

                j++;

            }

            if (nb != 0) {

                nbits[nsize] = nb;

                noffs[nsize] = no;

                nsize++;

            }

        }

        a.bits = nbits;

        a.offs = noffs;
