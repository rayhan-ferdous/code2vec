    public void println(String s) {

        try {

            output.write(s);

            output.newLine();

            allText.append(s + "\n");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
