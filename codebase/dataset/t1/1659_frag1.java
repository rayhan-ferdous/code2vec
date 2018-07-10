        public int read() throws java.io.IOException {

            if (position < 0) {

                if (encode) {

                    byte[] b3 = new byte[3];

                    int numBinaryBytes = 0;

                    for (int i = 0; i < 3; i++) {

                        try {

                            int b = in.read();

                            if (b >= 0) {

                                b3[i] = (byte) b;

                                numBinaryBytes++;

                            }

                        } catch (java.io.IOException e) {

                            if (i == 0) throw e;

                        }

                    }

                    if (numBinaryBytes > 0) {

                        encode3to4(b3, 0, numBinaryBytes, buffer, 0, options);
