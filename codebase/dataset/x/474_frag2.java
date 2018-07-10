    public static void closeOutput(BufferedWriter bw) {

        try {

            bw.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
