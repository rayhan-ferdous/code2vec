        public void flushBase64() throws java.io.IOException {

            if (position > 0) {

                if (encode) {

                    out.write(encode3to4(b4, buffer, position, options));

                    position = 0;

                } else {

                    throw new java.io.IOException("Base64 input not properly padded.");

                }

            }

        }
