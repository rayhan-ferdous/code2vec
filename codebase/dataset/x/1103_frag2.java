    public URL find(String classname) {

        try {

            URLConnection con = openClassfile0(classname);

            InputStream is = con.getInputStream();

            if (is != null) {

                is.close();

                return con.getURL();

            }

        } catch (IOException e) {

        }

        return null;

    }
