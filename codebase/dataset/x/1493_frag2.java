    public void writeClass(String filename, String code) {

        File wrtFile = new File(cdir.getPath() + psep + filename);

        try {

            FileWriter wrtWriter = new FileWriter(wrtFile);

            wrtWriter.write(code);

            wrtWriter.close();

        } catch (IOException e) {

            System.out.println("File Not Found. - " + e);

        }

    }
