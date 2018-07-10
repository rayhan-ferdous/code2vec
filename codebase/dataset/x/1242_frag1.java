    protected void normalize(float[] data) {

        float sum = 0;

        for (int i = 0; i < data.length; i++) {

            sum += data[i];

        }

        if (sum != 0.0f) {

            for (int i = 0; i < data.length; i++) {

                data[i] = data[i] / sum;

            }

        }

    }
