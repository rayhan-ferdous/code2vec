    static void readPackageList(InputStream input, String path, boolean relative) throws IOException {

        InputStreamReader in = new InputStreamReader(input);

        StringBuffer strbuf = new StringBuffer();

        try {

            int c;

            while ((c = in.read()) >= 0) {

                char ch = (char) c;

                if (ch == '\n' || ch == '\r') {

                    if (strbuf.length() > 0) {

                        String packname = strbuf.toString();

                        String packpath = path + packname.replace('.', '/') + '/';

                        new Extern(packname, packpath, relative);

                        strbuf.setLength(0);

                    }

                } else {

                    strbuf.append(ch);

                }

            }

        } finally {

            input.close();

        }

    }
