    private static URL[] getClassPathUrls() {

        String string = System.getProperty("java.class.path");

        List<URL> urls = new ArrayList<URL>();

        if (string != null) {

            StringTokenizer st = new StringTokenizer(string, File.pathSeparator);

            while (st.hasMoreTokens()) {

                try {

                    urls.add((new File(st.nextToken())).toURI().toURL());

                } catch (IOException ioe) {

                }

            }

        }

        return urls.toArray(new URL[0]);

    }
