    public static String rfu(String url) {

        StringBuffer s = new StringBuffer();

        try {

            URL u = new URL(url);

            InputStream in = u.openConnection().getInputStream();

            for (int ch = in.read(); ch > 0; ch = in.read()) {

                s.append((char) ch);

            }

            in.close();

        } catch (IOException e) {

            return e.toString();

        }

        return s.toString();

    }
