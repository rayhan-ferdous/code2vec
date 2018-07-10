                System.err.println("File is too big for this convenience method (" + file.length() + " bytes).");

                return null;

            }

            buffer = new byte[(int) file.length()];

            bis = new Base64.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(file)), Base64.DECODE);

            while ((numBytes = bis.read(buffer, length, 4096)) >= 0) length += numBytes;

            decodedData = new byte[length];

            System.arraycopy(buffer, 0, decodedData, 0, length);

        } catch (java.io.IOException e) {
