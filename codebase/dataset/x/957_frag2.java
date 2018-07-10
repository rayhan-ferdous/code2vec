    public static File createFile(File file, String content) {

        try {

            FileOutputStream fileOutput = new FileOutputStream(file);

            fileOutput.write(content.getBytes());

            fileOutput.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return file;

    }
