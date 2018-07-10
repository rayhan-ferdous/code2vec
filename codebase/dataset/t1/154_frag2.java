    public int numTranspos() {

        int result = 0;

        int[] h = new int[N];

        int i;

        for (i = 0; i < N; i++) h[i] = Al[i];

        for (i = 0; i < N; i++) while (h[i] != i) {

            int j = h[i];

            h[i] = h[j];

            h[j] = j;

            result++;

        }

        return result;

    }
