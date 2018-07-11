    private String readstream(InputStream in) {

        StringBuffer resultString = new StringBuffer();

        try {

            BufferedReader inbuff = new BufferedReader(new InputStreamReader(in, "GB2312"));

            String line = "";

            while ((line = inbuff.readLine()) != null) {

                resultString.append('\n');

                resultString.append(line);

            }

        } catch (Exception e) {

        }

        return resultString.toString();

    }
