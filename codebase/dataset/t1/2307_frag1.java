    public void setOutput(Writer writer) {

        if (writer == null) {

            output = new OutputStreamWriter(System.out);

        } else {

            output = writer;

        }

    }
