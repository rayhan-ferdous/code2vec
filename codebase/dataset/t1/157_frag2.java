    public int checkDominance(final float[] a1, final float[] a2) {

        boolean A1DomA2 = ((a1[0] - a2[0]) > 0);

        boolean A2DomA1 = ((a1[0] - a2[0]) < 0);

        for (int i = 1; i < a1.length; i++) {

            if (A1DomA2) {

                if ((a1[i] - a2[i]) < 0) {

                    A1DomA2 = false;

                    break;

                }

            } else if (A2DomA1) {

                if ((a1[i] - a2[i]) > 0) {

                    A2DomA1 = false;

                    break;

                }

            } else {

                A1DomA2 = ((a1[i] - a2[i]) > 0);

                A2DomA1 = ((a1[i] - a2[i]) < 0);

            }

        }

        if (A1DomA2) return (1); else if (A2DomA1) return (-1); else return (0);

    }



    public float[][][] calTPDistinction() {
