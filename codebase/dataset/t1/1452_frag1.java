            out.write(cr);

            if (passphrase != null) {

                out.write(header[0]);

                out.write(cr);

                out.write(header[1]);

                for (int i = 0; i < iv.length; i++) {

                    out.write(b2a((byte) ((iv[i] >>> 4) & 0x0f)));

                    out.write(b2a((byte) (iv[i] & 0x0f)));

                }

                out.write(cr);

                out.write(cr);

            }

            int i = 0;

            while (i < prv.length) {

                if (i + 64 < prv.length) {

                    out.write(prv, i, 64);

                    out.write(cr);

                    i += 64;

                    continue;

                }

                out.write(prv, i, prv.length - i);

                out.write(cr);

                break;

            }

            out.write(end);
