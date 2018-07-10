        public void write(int theByte) throws java.io.IOException {

            if (suspendEncoding) {

                super.out.write(theByte);

                return;

            }

            if (encode) {

                buffer[position++] = (byte) theByte;

                if (position >= bufferLength) {

                    out.write(encode3to4(b4, buffer, bufferLength, options));

                    lineLength += 4;

                    if (breakLines && lineLength >= MAX_LINE_LENGTH) {

                        out.write(NEW_LINE);

                        lineLength = 0;

                    }

                    position = 0;

                }

            } else {

                if (decodabet[theByte & 0x7f] > WHITE_SPACE_ENC) {

                    buffer[position++] = (byte) theByte;

                    if (position >= bufferLength) {

                        int len = Base64.decode4to3(buffer, 0, b4, 0, options);

                        out.write(b4, 0, len);

                        position = 0;

                    }

                } else if (decodabet[theByte & 0x7f] != WHITE_SPACE_ENC) {

                    throw new java.io.IOException("Invalid character in Base64 data.");
