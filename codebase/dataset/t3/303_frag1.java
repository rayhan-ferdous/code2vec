    public static void copyFromFileToFile(File inputFile, File outputFile) throws IOException {

        FileInputStream inputStream = new FileInputStream(inputFile);

        FileOutputStream outputStream = new FileOutputStream(outputFile);

        try {

            byte[] buffer = new byte[4096];

            int bytesRead = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {

                outputStream.write(buffer, 0, bytesRead);

            }

        } finally {

            inputStream.close();

            outputStream.close();

        }

    }
