    private void writeFile(File f, String s) {

        try {

            FileWriter fw = new FileWriter(f);

            fw.write(s);

            fw.flush();

            fw.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
