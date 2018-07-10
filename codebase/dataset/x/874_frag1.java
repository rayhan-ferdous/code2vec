    public void print(String s) {

        try {

            output.write(s);

            allText.append(s);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
