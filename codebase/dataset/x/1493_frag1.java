    public static void wtf_ru(String str, String furl, String enc) {

        try {

            FileOutputStream fos = new FileOutputStream(furl);

            Writer out = new OutputStreamWriter(fos, enc);

            out.write(str);

            out.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
