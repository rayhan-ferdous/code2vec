        int gzip = options & GZIP;

        if (gzip == GZIP) {

            java.io.ByteArrayOutputStream baos = null;

            java.util.zip.GZIPOutputStream gzos = null;

            Base64.OutputStream b64os = null;

            try {

                baos = new java.io.ByteArrayOutputStream();

                b64os = new Base64.OutputStream(baos, ENCODE | dontBreakLines);

                gzos = new java.util.zip.GZIPOutputStream(b64os);

                gzos.write(source, off, len);

                gzos.close();

            } catch (java.io.IOException e) {

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

        }
