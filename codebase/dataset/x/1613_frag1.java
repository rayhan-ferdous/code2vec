    public HttpResponse getOneAdver(int id) {

        try {

            HttpPost httpPost = new HttpPost(CENTER_SERVICE_URL);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            JSONObject params = new JSONObject();

            params.put("adverid", id);

            JSONObject json = new JSONObject();

            json.put("method", "getOneAdver");

            json.put("key", KEY);

            json.put("params", params);

            nameValuePairs.add(new BasicNameValuePair("service", json.toString()));

            Log.e(ContextApplication.TAG, "getOneAdver: " + json.toString());

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

            return executeHttpRequest(httpPost);

        } catch (Exception e) {

        }

        return null;

    }
