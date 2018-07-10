    public static Object[] loadTextFile(final String filename) {

        List lData = new ArrayList();

        String feed = null;

        BufferedReader in = null;

        try {

            in = new BufferedReader(new FileReader(filename));

            while ((feed = in.readLine()) != null) {

                feed = feed.trim();

                if (feed != null) {

                    lData.add(feed);

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (in != null) {

                try {

                    in.close();

                } catch (IOException e) {

                }

            }

        }

        return lData.toArray();

    }
