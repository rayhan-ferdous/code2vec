                    out.write(PXDATA_TAG);

                    out.write((byte) pxlen);

                    out.write((byte) (pxlen >> 8));

                    out.write((byte) (pxlen >> 16));

                    out.write((byte) (pxlen >> 24));

                }
