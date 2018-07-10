    public static boolean encodeToFile(byte[] dataToEncode, String filename) {

        boolean success = false;

        Base64.OutputStream bos = null;

        try {

            bos = new Base64.OutputStream(new java.io.FileOutputStream(filename), Base64.ENCODE);

            bos.write(dataToEncode);

            success = true;

        } catch (java.io.IOException e) {

            success = false;

        } finally {

            try {

                bos.close();

            } catch (Exception e) {

            }

        }

        return success;

    }
