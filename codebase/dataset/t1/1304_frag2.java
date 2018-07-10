    public void sort(final long off, final long len) {

        if (len < 7) {

            for (long i = off; i < len + off; i++) {

                for (long j = i; j > off && get(j - 1) > get(j); j--) {

                    swap(j, j - 1);

                }

            }

            return;

        }

        long m = off + (len >> 1);

        if (len > 7) {

            long l = off;

            long n = off + len - 1;

            if (len > 40) {

                long s = len / 8;

                l = med3(l, l + s, l + 2 * s);

                m = med3(m - s, m, m + s);

                n = med3(n - 2 * s, n - s, n);

            }

            m = med3(l, m, n);

        }

        final byte v = get(m);

        long a = off, b = a, c = off + len - 1, d = c;

        while (true) {

            while (b <= c && get(b) <= v) {

                if (get(b) == v) {

                    swap(a++, b);

                }

                b++;

            }

            while (c >= b && get(c) >= v) {

                if (get(c) == v) {

                    swap(c, d--);

                }

                c--;

            }

            if (b > c) {

                break;

            }

            swap(b++, c--);

        }

        long s, n = off + len;

        s = Math.min(a - off, b - a);

        vecswap(off, b - s, s);

        s = Math.min(d - c, n - d - 1);

        vecswap(b, n - s, s);

        if ((s = b - a) > 1) {

            sort(off, s);

        }

        if ((s = d - c) > 1) {

            sort(n - s, s);

        }

    }
