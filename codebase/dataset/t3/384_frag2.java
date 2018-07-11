    public int putParameters(Hashtable<String, String> params) {

        try {

            String data = "";

            Enumeration<String> senum = params.keys();

            while (senum.hasMoreElements()) {

                String key = senum.nextElement();

                data += (data.length() > 0) ? "&" : "";

                data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8");

            }

            con.getOutputStream().write(data.getBytes("UTF-8"));

        } catch (Exception e) {

            e.printStackTrace();

            return -1;

        }

        return 1;

    }
