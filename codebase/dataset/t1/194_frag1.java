        int i, m, mpbegin, end;

        if (nrow <= 0) return;

        m = nrow % 5;

        mpbegin = m + begin;

        end = begin + nrow - 1;

        for (i = begin; i < mpbegin; i++) {

            x[i][j] *= a;

        }

        for (i = mpbegin; i <= end; i += 5) {

            x[i][j] *= a;

            x[i + 1][j] *= a;

            x[i + 2][j] *= a;

            x[i + 3][j] *= a;

            x[i + 4][j] *= a;

        }

        return;
