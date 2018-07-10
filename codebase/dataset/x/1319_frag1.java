    public static String makeFullPathName(String resultsDirectory, String fileName) {

        File dump = new File(resultsDirectory, fileName);

        String fullPathName = "";

        try {

            fullPathName = dump.getCanonicalPath();

        } catch (IOException e) {

            System.out.println("Problem building text to write files!");

            e.printStackTrace();

        }

        return fullPathName;

    }
