    private static void encrypt(byte[] blck, int edflag) {

        byte[] p = blck;

        transpose(p, InitialTr, 64);

        for (int i = 15; i >= 0; i--) {

            int j = edflag > 0 ? i : 15 - i;

            byte[] b = new byte[64];

            System.arraycopy(p, 0, b, 0, b.length);

            byte[] x = new byte[64];

            for (int k = 31; k >= 0; k--) {

                p[k] = b[k + 32];

            }

            f(j, key, p, x);

            for (int k = 31; k >= 0; k--) {

                p[k + 32] = (byte) (b[k] ^ x[k]);

            }

        }

        transpose(p, swap, 64);

        transpose(p, FinalTr, 64);

        blck = p;

    }
