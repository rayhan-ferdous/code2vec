    @Override

    public OutputStream getOutputStream(String path) {

        OutputStream os = null;

        if (path.startsWith("/")) {

            path = path.substring(1);

        }

        try {

            os = new FileOutputStream(path);

        } catch (SecurityException e) {

            e.printStackTrace();

        } catch (FileNotFoundException e) {

            os = null;

        }

        return os;

    }
