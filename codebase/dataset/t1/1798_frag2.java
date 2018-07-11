            if (swap && (len & 1) != 0) {

                throw new DcmParseException("Illegal length of OW Pixel Data: " + len);

            }

            if (buffer == null) {

                if (swap) {

                    int tmp;

                    for (int i = 0; i < len; ++i, ++i) {

                        tmp = in.read();

                        out.write(in.read());

                        out.write(tmp);

                    }

                } else {

                    for (int i = 0; i < len; ++i) {

                        out.write(in.read());

                    }

                }

            } else {
