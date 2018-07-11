    public static FileInputStream getFileInputStream(String fileName) {

        try {

            return new FileInputStream(fileName);

        } catch (FileNotFoundException e) {

            throw new IORuntimeException(e);

        }

    }
