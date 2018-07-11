    public static void writeByteArray(byte[] data, File file) {

        try {

            FileOutputStream fileOut = new FileOutputStream(file);

            fileOut.write(data);

            fileOut.flush();

            fileOut.close();

        } catch (IOException e) {

            logger.log(Level.SEVERE, "Exception thrown in writeByteArray(byte[] data, File file)", e);

        }

    }
