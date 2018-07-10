    public DataInputStream getDataInpuStream(File file) {

        DataInputStream ds = null;

        try {

            ds = new DataInputStream(new FileInputStream(file));

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        return ds;

    }
