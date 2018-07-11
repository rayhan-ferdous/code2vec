    private void floorData(float[] data, float floor) {

        for (int i = 0; i < data.length; i++) {

            if (data[i] < floor) {

                data[i] = floor;

            }

        }

    }
