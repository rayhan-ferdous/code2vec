    public static String readTextFromJar(String s) {

        InputStream is = null;

        BufferedReader br = null;

        String line = null;

        try {

            is = RandomInfo.class.getResourceAsStream(s);

            br = new BufferedReader(new InputStreamReader(is));

            line = br.readLine();

            if (br != null) {

                br.close();

            }

            if (is != null) {

                is.close();

            }

        } catch (Exception e) {

        }

        return line;

    }
