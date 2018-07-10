    public boolean isEven() {

        int[] h = new int[N];

        int i;

        boolean result = true;

        for (i = 0; i < N; i++) h[i] = Al[i];

        for (i = 0; i < N; i++) while (h[i] != i) {

            int j = h[i];

            h[i] = h[j];

            h[j] = j;

            result = !result;

        }

        return result;

    }
