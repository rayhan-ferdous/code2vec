                e.printStackTrace();

                return null;

            } finally {

                try {

                    gzos.close();

                } catch (Exception e) {

                }

                try {

                    b64os.close();

                } catch (Exception e) {

                }

                try {

                    baos.close();

                } catch (Exception e) {

                }

            }

            try {

                return new String(baos.toByteArray(), PREFERRED_ENCODING);

            } catch (java.io.UnsupportedEncodingException uue) {

                return new String(baos.toByteArray());

            }

        } else {

            boolean breakLines = dontBreakLines == 0;

            int len43 = len * 4 / 3;

            byte[] outBuff = new byte[(len43) + ((len % 3) > 0 ? 4 : 0) + (breakLines ? (len43 / MAX_LINE_LENGTH) : 0)];

            int d = 0;

            int e = 0;

            int len2 = len - 2;

            int lineLength = 0;

            for (; d < len2; d += 3, e += 4) {

                encode3to4(source, d + off, 3, outBuff, e, options);

                lineLength += 4;

                if (breakLines && lineLength == MAX_LINE_LENGTH) {

                    outBuff[e + 4] = NEW_LINE;

                    e++;

                    lineLength = 0;

                }

            }

            if (d < len) {

                encode3to4(source, d + off, len - d, outBuff, e, options);

                e += 4;

            }

            try {

                return new String(outBuff, 0, e, PREFERRED_ENCODING);

            } catch (java.io.UnsupportedEncodingException uue) {

                return new String(outBuff, 0, e);

            }

        }

    }
