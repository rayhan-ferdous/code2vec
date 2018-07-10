    public int putParameterMap(Map params) {

        try {

            String data = "";

            Iterator it = params.keySet().iterator();

            while (it.hasNext()) {

                String key = (String) it.next();

                data += (data.length() > 0) ? "&" : "";

                data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(CoreUtils.collapseArray((String[]) params.get(key)), "UTF-8");

            }

            System.out.println("data: " + data);

            con.getOutputStream().write(data.getBytes("UTF-8"));

        } catch (Exception e) {

            e.printStackTrace();

            return -1;

        }

        return 1;

    }
