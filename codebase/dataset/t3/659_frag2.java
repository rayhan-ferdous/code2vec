                        int grlen = pxlen + 8;

                        out.write((byte) grlen);

                        out.write((byte) (grlen >> 8));

                        out.write((byte) (grlen >> 16));

                        out.write((byte) (grlen >> 24));

                    }
