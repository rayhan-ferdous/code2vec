                byte[] buffer = new byte[2048];

                int length = 0;

                try {

                    baos = new java.io.ByteArrayOutputStream();

                    bais = new java.io.ByteArrayInputStream(bytes);

                    gzis = new java.util.zip.GZIPInputStream(bais);

                    while ((length = gzis.read(buffer)) >= 0) {

                        baos.write(buffer, 0, length);

                    }

                    bytes = baos.toByteArray();

                } catch (java.io.IOException e) {
