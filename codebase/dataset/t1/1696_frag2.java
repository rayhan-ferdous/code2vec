        if (gzip == GZIP) {

            java.io.ByteArrayOutputStream baos = null;

            java.util.zip.GZIPOutputStream gzos = null;

            Base64.OutputStream b64os = null;

            try {

                baos = new java.io.ByteArrayOutputStream();

                b64os = new Base64.OutputStream(baos, ENCODE | options);

                gzos = new java.util.zip.GZIPOutputStream(b64os);

                gzos.write(source, off, len);

                gzos.close();

            } catch (java.io.IOException e) {
