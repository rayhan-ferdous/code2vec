            bis = new Base64.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(file)), Base64.ENCODE);

            while ((numBytes = bis.read(buffer, length, 4096)) >= 0) length += numBytes;

            encodedData = new String(buffer, 0, length, Base64.PREFERRED_ENCODING);
