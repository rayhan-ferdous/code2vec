        public static void copyMatrix(double[][] in, Double[][] out) {

            int rowC = in.length;

            for (int rowI = 0; rowI < rowC; rowI++) {

                int colC = in[rowI].length;

                for (int colI = 0; colI < colC; colI++) {

                    out[rowI][colI] = in[rowI][colI];

                }

            }

        }
